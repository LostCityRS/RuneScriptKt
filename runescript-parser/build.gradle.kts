plugins {
    kotlin("jvm")
    antlr
}

val antlrSource = "src/main/antlr"
val antlrOutput = "src/main/java/me/filby/neptune/runescript/antlr"
val antlrPackage = "me.filby.neptune.runescript.antlr"

dependencies {
    antlr(libs.antlr)
}

tasks.compileKotlin {
    // depend on the grammar source generation, this is implicit for compileJava but not compileKotlin
    dependsOn(tasks.generateGrammarSource)
}

task("antlrOutputDir") {
    doLast {
        mkdir(antlrOutput)
    }
}

tasks.sourcesJar {
    dependsOn(tasks.generateGrammarSource)
}

tasks.generateGrammarSource {
    dependsOn("antlrOutputDir")

    outputDirectory = file(antlrOutput)

    val grammars = fileTree(antlrSource) { include("**/*.g4") }.files.map { it.toString() }
    arguments = listOf("-no-listener", "-visitor", "-package", antlrPackage) + grammars
}
