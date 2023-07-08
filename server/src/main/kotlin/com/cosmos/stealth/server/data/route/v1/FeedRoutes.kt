package com.cosmos.stealth.server.data.route.v1

import com.cosmos.stealth.core.model.api.FeedRequest
import com.cosmos.stealth.server.data.service.FeedService
import com.cosmos.stealth.server.util.extension.info
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject

fun Route.feedRouting() {
    val feedService by inject<FeedService>()

    post("/feed") {
        val feedRequest = call.receive<FeedRequest>()
        val feed = feedService.getFeed(call.info, feedRequest)

        call.respond(HttpStatusCode.OK, feed)
    }
}
