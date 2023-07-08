package com.cosmos.stealth.server.data.route.v1

import com.cosmos.stealth.server.data.service.ServicesService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject

fun Route.servicesRouting() {
    val servicesService by inject<ServicesService>()

    get("/services") {
        val services = servicesService.getServices()

        call.respond(HttpStatusCode.OK, services)
    }
}
