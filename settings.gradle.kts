pluginManagement {
    val kotlin = "1.8.22"
    val ktor = "2.3.1"

    plugins {
        kotlin("jvm") version kotlin apply false
        kotlin("kapt") version kotlin apply false
        id("io.ktor.plugin") version ktor apply false
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
include(":core:model")
include(":core:network")
include(":services:base")
include(":services:reddit")
include(":services:teddit")
