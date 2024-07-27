package me.filby.neptune.clientscript.compiler.writer

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import me.filby.neptune.runescript.compiler.codegen.script.RuneScript
import java.io.OutputStream
import java.nio.file.Path
import java.nio.file.StandardOpenOption.CREATE
import java.nio.file.StandardOpenOption.TRUNCATE_EXISTING
import java.nio.file.StandardOpenOption.WRITE
import java.util.TreeMap
import kotlin.io.path.absolute
import kotlin.io.path.createDirectories
import kotlin.io.path.isDirectory
import kotlin.io.path.notExists
import kotlin.io.path.outputStream

/**
 * A [BinaryFileScriptWriter] implementation that writes to `script.dat` and `script.idx`.
 */
class JagFileScriptWriter(
    private val output: Path,
    idProvider: IdProvider,
    allocator: ByteBufAllocator = ByteBufAllocator.DEFAULT,
) : BinaryScriptWriter(idProvider, allocator) {
    private val buffers = TreeMap<Int, ByteBuf>()

    init {
        if (output.notExists()) {
            output.createDirectories()
        }
        require(output.isDirectory()) { "${output.absolute()} is not a directory." }
    }

    override fun outputScript(script: RuneScript, data: ByteBuf) {
        val id = idProvider.get(script.symbol)
        buffers[id] = data.retain()
    }

    override fun close() {
        val dat = output.resolve("script.dat").outputStream(WRITE, TRUNCATE_EXISTING, CREATE)
        val idx = output.resolve("script.idx").outputStream(WRITE, TRUNCATE_EXISTING, CREATE)
        try {
            val lastId = buffers.lastKey() ?: 0

            // write the number of entries (including gaps)
            dat.p2(lastId + 1)
            idx.p2(lastId + 1)

            // write version to dat file
            dat.p4(VERSION)

            for (i in 0..lastId) {
                val buffer = buffers[i]
                if (buffer == null) {
                    // gap, no size
                    idx.p2(0)
                    continue
                }

                // write the data to dat, and size to idx
                val size = buffer.readableBytes()
                idx.p2(size)
                buffer.readBytes(dat, size)
                buffer.release()
            }
        } finally {
            dat.close()
            idx.close()
        }
    }

    private fun OutputStream.p2(num: Int) {
        write(num shr 8)
        write(num)
    }

    private fun OutputStream.p4(num: Int) {
        write(num shr 24)
        write(num shr 16)
        write(num shr 8)
        write(num)
    }

    private companion object {
        const val VERSION = 19
    }
}
