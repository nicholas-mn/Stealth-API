plugins {
    kotlin("jvm") version Dependencies.Versions.kotlin
    id("io.ktor.plugin") version Dependencies.Versions.ktor
}

group = Config.group
version = Config.version
application {
    mainClass.set("com.cosmos.stealth.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:${Dependencies.Versions.ktor}")
    implementation("io.ktor:ktor-server-netty-jvm:${Dependencies.Versions.ktor}")

    implementation("ch.qos.logback:logback-classic:${Dependencies.Versions.logback}")

    testImplementation("io.ktor:ktor-server-tests-jvm:${Dependencies.Versions.ktor}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Dependencies.Versions.kotlin}")
}