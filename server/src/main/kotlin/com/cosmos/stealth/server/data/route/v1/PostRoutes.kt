package com.cosmos.stealth.server.data.route.v1

import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.data.Default
import com.cosmos.stealth.core.model.data.Filtering
import com.cosmos.stealth.core.model.data.Path
import com.cosmos.stealth.core.model.data.PostRequest
import com.cosmos.stealth.server.data.service.PostService
import com.cosmos.stealth.server.util.extension.info
import com.cosmos.stealth.server.util.extension.respondWithResource
import com.cosmos.stealth.server.util.getAfter
import com.cosmos.stealth.server.util.getInstance
import com.cosmos.stealth.server.util.getLimit
import com.cosmos.stealth.server.util.getOrder
import com.cosmos.stealth.server.util.getPost
import com.cosmos.stealth.server.util.getService
import com.cosmos.stealth.server.util.getSort
import io.ktor.server.application.call
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject

fun Route.postRouting() {
    val postService by inject<PostService>()

    get("/post/{${Path.POST}}") {
        val post = call.getPost()

        val serviceName = call.getService()

        val instance = call.getInstance()
        val sort = call.getSort()
        val order = call.getOrder()
        val limit = call.getLimit(Default.POST_LIMIT)
        val after = call.getAfter()

        val postRequest = PostRequest(
            call.info,
            post,
            Service(serviceName, instance),
            Filtering(sort, order),
            limit,
            after
        )

        val postResource = postService.getPost(postRequest)

        call.respondWithResource(postResource)
    }
}
