plugins {
    kotlin("jvm")
}

dependencies {
    implementation("io.insert-koin:koin-ktor:${Dependencies.Versions.koin}")
    implementation("org.jetbrains:markdown:${Dependencies.Versions.markdown}")
}
