plugins {
    kotlin("jvm")
    id("io.ktor.plugin")
    id("com.google.devtools.ksp")
}

val development: String by project

sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}

application {
    mainClass.set("com.cosmos.stealth.server.ApplicationKt")

    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$development")
}

ktor {
    val archiveName = "${rootProject.name}-${Config.version}"

    fatJar {
        archiveFileName.set("$archiveName.jar")
    }

    docker {
        jreVersion.set(JavaVersion.VERSION_17)
        localImageName.set(rootProject.name)
        imageTag.set(Config.version)
    }

    jib {
        outputPaths {
            tar = "build/$archiveName.tar"
            digest = "build/$archiveName.digest"
            imageJson = "build/$archiveName.json"
            imageId = "build/$archiveName.id"
        }
    }
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
    implementation("io.ktor:ktor-server-swagger:${Dependencies.Versions.ktor}")
    implementation("io.ktor:ktor-server-html-builder:${Dependencies.Versions.ktor}")
    implementation("io.ktor:ktor-server-caching-headers:${Dependencies.Versions.ktor}")

    implementation("io.insert-koin:koin-ktor:${Dependencies.Versions.koin}")
    implementation("io.insert-koin:koin-annotations:${Dependencies.Versions.koinKsp}")
    ksp("io.insert-koin:koin-ksp-compiler:${Dependencies.Versions.koinKsp}")

    implementation("com.squareup.okhttp3:okhttp:${Dependencies.Versions.okHttp}")

    implementation("ch.qos.logback:logback-classic:${Dependencies.Versions.logback}")
    implementation("org.codehaus.janino:janino:${Dependencies.Versions.janino}")

    testImplementation("io.ktor:ktor-server-tests-jvm:${Dependencies.Versions.ktor}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Dependencies.Versions.kotlin}")
}
