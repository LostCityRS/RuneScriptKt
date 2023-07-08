package me.filby.neptune.clientscript.compiler

import me.filby.neptune.clientscript.compiler.writer.JagFileScriptWriter
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readLines

fun main(args: Array<String>) {
    if (args.size != 2) {
        error("usage: compiler.jar [src path] [output path]")
    }

    val srcPath = Path(args[0])
    if (!srcPath.exists()) {
        error("$srcPath does not exist.")
    }

    val outputPath = Path(args[1])

    val mapper = SymbolMapper()

    // TODO move somewhere else?
    val scriptMappings = Path("symbols/runescript.tsv")
    if (scriptMappings.exists()) {
        for (line in scriptMappings.readLines()) {
            val split = line.split("\t")
            val id = split[0].toInt()
            val name = split[1]
            mapper.putScript(id, name)
        }
    }

    val writer = JagFileScriptWriter(outputPath, mapper)
    val compiler = ClientScriptCompiler(srcPath, writer, mapper)
    compiler.setup()
    compiler.run()
}
