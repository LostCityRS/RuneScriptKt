plugins {
    application
    kotlin("jvm")
}

dependencies {
    api(project(":runescript-compiler")) {
        exclude("com.ibm.icu", "icu4j")
    }
    implementation(libs.netty.buffer)
    implementation(libs.fourkoma)
    runtimeOnly(libs.logback)
}

application {
    applicationName = "cs2"
    mainClass.set("me.filby.neptune.clientscript.compiler.ClientScriptCompilerApplicationKt")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "me.filby.neptune.clientscript.compiler.ClientScriptCompilerApplicationKt"
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}
