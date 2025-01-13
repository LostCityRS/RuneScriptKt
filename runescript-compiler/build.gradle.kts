plugins {
    kotlin("jvm")
}

dependencies {
    api(project(":runescript-parser"))
    runtimeOnly(libs.logback)
}

kotlin {
    explicitApi()
}
