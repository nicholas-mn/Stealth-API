package com.cosmos.stealth.server.data.route

import com.cosmos.stealth.server.data.route.v1.v1
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        v1()
    }
}
