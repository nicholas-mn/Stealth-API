plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    implementation("com.squareup.moshi:moshi:${Dependencies.Versions.moshi}")
    implementation("com.squareup.moshi:moshi-adapters:${Dependencies.Versions.moshi}")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Dependencies.Versions.moshi}")
}
