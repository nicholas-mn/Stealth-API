pluginManagement {
    val kotlin = "1.9.10"
    val ktor = "2.3.4"
    val ksp = "1.9.10-1.0.13"

    plugins {
        kotlin("jvm") version kotlin apply false
        kotlin("plugin.serialization") version kotlin apply false
        id("io.ktor.plugin") version ktor apply false
        id("com.google.devtools.ksp") version ksp apply false
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "stealth-api"

include(":server")

include(":core:common")
include(":core:data")
include(":core:model")
include(":core:network")

include(":services:base")
include(":services:lemmy")
include(":services:reddit")
include(":services:teddit")
