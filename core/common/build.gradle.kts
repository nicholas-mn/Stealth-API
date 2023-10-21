plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}

dependencies {
    implementation("io.insert-koin:koin-ktor:${Dependencies.Versions.koin}")
    implementation("io.insert-koin:koin-annotations:${Dependencies.Versions.koinKsp}")
    ksp("io.insert-koin:koin-ksp-compiler:${Dependencies.Versions.koinKsp}")
    implementation("org.jetbrains:markdown:${Dependencies.Versions.markdown}")
}
