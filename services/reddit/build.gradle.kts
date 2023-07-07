plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":services:base"))

    implementation("io.ktor:ktor-client-core:${Dependencies.Versions.ktor}")
    implementation("io.ktor:ktor-client-content-negotiation:${Dependencies.Versions.ktor}")
    implementation("io.ktor:ktor-client-okhttp:${Dependencies.Versions.ktor}")

    implementation("io.insert-koin:koin-ktor:${Dependencies.Versions.koin}")

    implementation("com.squareup.moshi:moshi:${Dependencies.Versions.moshi}")
    implementation("com.squareup.moshi:moshi-adapters:${Dependencies.Versions.moshi}")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Dependencies.Versions.moshi}")

    implementation("org.jsoup:jsoup:${Dependencies.Versions.jsoup}")
}
