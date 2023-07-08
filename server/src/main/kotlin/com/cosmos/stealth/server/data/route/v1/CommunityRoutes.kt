package com.cosmos.stealth.server.data.route.v1

import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.server.data.service.CommunityService
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

fun Route.communityRouting() {
    val communityService by inject<CommunityService>()

    route("/community") {

        get("/{community}") {
            val community = call.getPath("community") ?: error("Community name is required")
            val service = call.getQuery("service") ?: error("Service is required")

            val serviceName = ServiceName.decode(service) ?: error("Unknown service $service")

            val instance = call.getQuery("instance")
            val sort = Sort.decode(call.getQuery("sort")) ?: Sort.best
            val after = call.getQuery("after")?.toAfterKey()

            val communityResource = communityService.getCommunity(
                call.info,
                community,
                Service(serviceName, instance),
                sort,
                after
            )

            call.respondWithResource(communityResource)
        }

        get("/{community}/info") {
            val community = call.getPath("community") ?: error("Community name is required")
            val service = call.getQuery("service") ?: error("Service is required")

            val serviceName = ServiceName.decode(service) ?: error("Unknown service $service")

            val instance = call.getQuery("instance")

            val communityInfo = communityService.getCommunityInfo(call.info, community, Service(serviceName, instance))

            call.respondWithResource(communityInfo)
        }
    }
}
