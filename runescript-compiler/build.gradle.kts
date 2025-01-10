plugins {
    kotlin("jvm")
}

dependencies {
    api(project(":runescript-parser"))
    runtimeOnly(libs.logback)

    testImplementation(project(":runescript-runtime"))
}

kotlin {
    explicitApi()
}
