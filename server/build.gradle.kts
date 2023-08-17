plugins {
    kotlin("jvm")
    id("io.ktor.plugin")
    id("com.google.devtools.ksp")
}

sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}

application {
    mainClass.set("com.cosmos.stealth.server.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:data"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":services:base"))
    implementation(project(":services:lemmy"))
    implementation(project(":services:reddit"))
    implementation(project(":services:teddit"))

    implementation("io.ktor:ktor-server-core-jvm:${Dependencies.Versions.ktor}")
    implementation("io.ktor:ktor-server-netty-jvm:${Dependencies.Versions.ktor}")
    implementation("io.ktor:ktor-server-content-negotiation:${Dependencies.Versions.ktor}")
    implementation("io.ktor:ktor-server-forwarded-header:${Dependencies.Versions.ktor}")
    implementation("io.ktor:ktor-server-status-pages:${Dependencies.Versions.ktor}")

    implementation("io.insert-koin:koin-ktor:${Dependencies.Versions.koin}")
    implementation("io.insert-koin:koin-annotations:${Dependencies.Versions.koinKsp}")
    ksp("io.insert-koin:koin-ksp-compiler:${Dependencies.Versions.koinKsp}")

    implementation("ch.qos.logback:logback-classic:${Dependencies.Versions.logback}")

    testImplementation("io.ktor:ktor-server-tests-jvm:${Dependencies.Versions.ktor}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Dependencies.Versions.kotlin}")
}
