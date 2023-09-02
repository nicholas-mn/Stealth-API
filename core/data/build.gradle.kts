plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
}

sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))

    implementation("io.ktor:ktor-client-core:${Dependencies.Versions.ktor}")
    implementation("io.ktor:ktor-client-content-negotiation:${Dependencies.Versions.ktor}")
    implementation("io.ktor:ktor-serialization-kotlinx-xml:${Dependencies.Versions.ktor}")
    implementation("io.ktor:ktor-client-okhttp:${Dependencies.Versions.ktor}")

    implementation("io.insert-koin:koin-ktor:${Dependencies.Versions.koin}")
    implementation("io.insert-koin:koin-annotations:${Dependencies.Versions.koinKsp}")
    ksp("io.insert-koin:koin-ksp-compiler:${Dependencies.Versions.koinKsp}")
}
