package com.cosmos.stealth.server.data.route.v1

import com.cosmos.stealth.core.model.api.MoreContentFeedable
import com.cosmos.stealth.core.model.data.MoreContentRequest
import com.cosmos.stealth.server.data.service.ContentService
import com.cosmos.stealth.server.util.extension.info
import com.cosmos.stealth.server.util.extension.respondWithResource
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.koin.ktor.ext.inject

fun Route.contentRouting() {
    val contentService by inject<ContentService>()

    post("/more") {
        val moreContentFeedable = call.receive<MoreContentFeedable>()

        val moreContentRequest = MoreContentRequest(call.info, moreContentFeedable)

        val feedables = contentService.getMoreContent(moreContentRequest)

        call.respondWithResource(feedables)
    }
}
