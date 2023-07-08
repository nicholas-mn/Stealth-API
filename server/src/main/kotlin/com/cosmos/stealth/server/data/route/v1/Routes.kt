package com.cosmos.stealth.server.data.route.v1

import io.ktor.server.routing.Routing
import io.ktor.server.routing.route

fun Routing.v1() {
    route("/v1") {
        feedRouting()
        communityRouting()
        userRouting()
        postRouting()
        searchRouting()
        servicesRouting()
    }
}
