package com.cosmos.stealth.server.data.route

import com.cosmos.stealth.server.data.route.v1.v1
import com.cosmos.stealth.server.util.swaggerUI
import io.ktor.server.application.Application
import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        swaggerUI("/", "stealth-api.yaml", "Stealth API")

        staticResources("/", "files")
        staticResources("/en", "en")

        v1()
    }
}
