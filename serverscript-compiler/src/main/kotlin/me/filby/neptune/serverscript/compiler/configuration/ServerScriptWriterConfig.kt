package me.filby.neptune.serverscript.compiler.configuration

import me.filby.neptune.serverscript.compiler.writer.BinaryFileScriptWriter

/**
 * Container for different script writers.
 */
data class ServerScriptWriterConfig(val binary: BinaryFileWriterConfig? = null)

/**
 * Configuration for [BinaryFileScriptWriter].
 */
data class BinaryFileWriterConfig(val outputPath: String)
