package me.filby.neptune.serverscript.compiler.configuration

/**
 * Main compiler configuration holder.
 */
data class ServerScriptCompilerConfig(
    val sourcePaths: List<String> = listOf("src/"),
    val symbolPaths: List<String> = listOf("symbols/"),
    val excludePaths: List<String> = emptyList(),
    val checkPointers: Boolean = true,
    val writers: ServerScriptWriterConfig = ServerScriptWriterConfig(),
)
