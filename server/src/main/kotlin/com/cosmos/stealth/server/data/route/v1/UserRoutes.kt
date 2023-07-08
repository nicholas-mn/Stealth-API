package com.cosmos.stealth.server.data.route.v1

import com.cosmos.stealth.core.model.api.FeedableType
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.server.data.service.UserService
import com.cosmos.stealth.server.util.extension.getPath
import com.cosmos.stealth.server.util.extension.getQuery
import com.cosmos.stealth.server.util.extension.info
import com.cosmos.stealth.server.util.extension.respondWithResource
import com.cosmos.stealth.services.base.util.extension.toAfterKey
import io.ktor.server.application.call
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.userRouting() {
    val userService by inject<UserService>()

    route("/user") {

        get("/{user}") {
            val user = call.getPath("user") ?: error("User id is required")
            val service = call.getQuery("service") ?: error("Service is required")

            val serviceName = ServiceName.decode(service) ?: error("Unknown service $service")

            val instance = call.getQuery("instance")
            val sort = Sort.decode(call.getQuery("sort")) ?: Sort.best
            val after = call.getQuery("after")?.toAfterKey()
            val type = FeedableType.decode(call.getQuery("type")) ?: FeedableType.post

            val userResource = userService.getUser(call.info, user, Service(serviceName, instance), sort, after, type)

            call.respondWithResource(userResource)
        }

        get("/{user}/info") {
            val user = call.getPath("user") ?: error("User id is required")
            val service = call.getQuery("service") ?: error("Service is required")

            val serviceName = ServiceName.decode(service) ?: error("Unknown service $service")

            val instance = call.getQuery("instance")

            val userInfo = userService.getUserInfo(call.info, user, Service(serviceName, instance))

            call.respondWithResource(userInfo)
        }
    }
}
