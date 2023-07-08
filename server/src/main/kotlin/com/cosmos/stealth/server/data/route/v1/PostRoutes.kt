package com.cosmos.stealth.server.data.route.v1

import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.server.data.service.PostService
import com.cosmos.stealth.server.util.extension.getPath
import com.cosmos.stealth.server.util.extension.getQuery
import com.cosmos.stealth.server.util.extension.info
import com.cosmos.stealth.server.util.extension.respondWithResource
import io.ktor.server.application.call
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject

fun Route.postRouting() {
    val postService by inject<PostService>()

    get("/post/{post}") {
        val post = call.getPath("post") ?: error("Post ID is required")
        val service = call.getQuery("service") ?: error("Service is required")

        val serviceName = ServiceName.decode(service) ?: error("Unknown service $service")

        val instance = call.getQuery("instance")
        val sort = Sort.decode(call.getQuery("sort")) ?: Sort.best

        val postResource = postService.getPost(call.info, post, Service(serviceName, instance), sort)

        call.respondWithResource(postResource)
    }
}
