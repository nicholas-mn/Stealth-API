plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}

sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}

dependencies {
    implementation("com.squareup.moshi:moshi:${Dependencies.Versions.moshi}")
    implementation("com.squareup.moshi:moshi-adapters:${Dependencies.Versions.moshi}")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:${Dependencies.Versions.moshi}")
}
