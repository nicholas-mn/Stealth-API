package com.cosmos.stealth.server.data.route.v1

import io.ktor.server.routing.Route
import io.ktor.server.routing.route

fun Route.v1() {
    route("/v1") {
        feedRouting()
        communityRouting()
        userRouting()
        postRouting()
        contentRouting()
        searchRouting()
        servicesRouting()
    }
}
