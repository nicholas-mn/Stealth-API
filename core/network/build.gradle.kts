plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementation("org.jetbrains.kotlin:kotlin-reflect:${Dependencies.Versions.kotlin}")

    implementation("io.ktor:ktor-client-content-negotiation:${Dependencies.Versions.ktor}")

    implementation("io.insert-koin:koin-ktor:${Dependencies.Versions.koin}")

    implementation("com.squareup.moshi:moshi:${Dependencies.Versions.moshi}")
    implementation("com.squareup.moshi:moshi-adapters:${Dependencies.Versions.moshi}")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Dependencies.Versions.moshi}")

    implementation("com.squareup.okio:okio:${Dependencies.Versions.okio}")
    implementation("com.squareup.okhttp3:okhttp:${Dependencies.Versions.okHttp}")

    implementation("org.jsoup:jsoup:${Dependencies.Versions.jsoup}")

    implementation("org.apache.commons:commons-text:${Dependencies.Versions.apacheText}")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Dependencies.Versions.kotlin}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Dependencies.Versions.coroutines}")
}
