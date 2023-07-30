plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.serialization")
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
}
