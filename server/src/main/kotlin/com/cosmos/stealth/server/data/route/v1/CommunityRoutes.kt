package com.cosmos.stealth.server.data.route.v1

import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.data.CommunityInfoRequest
import com.cosmos.stealth.core.model.data.CommunityRequest
import com.cosmos.stealth.core.model.data.Filtering
import com.cosmos.stealth.core.model.data.Path
import com.cosmos.stealth.server.data.service.CommunityService
import com.cosmos.stealth.server.util.extension.info
import com.cosmos.stealth.server.util.extension.respondWithResource
import com.cosmos.stealth.server.util.getAfter
import com.cosmos.stealth.server.util.getCommunity
import com.cosmos.stealth.server.util.getInstance
import com.cosmos.stealth.server.util.getLimit
import com.cosmos.stealth.server.util.getOrder
import com.cosmos.stealth.server.util.getService
import com.cosmos.stealth.server.util.getSort
import com.cosmos.stealth.server.util.getTime
import io.ktor.server.application.call
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.communityRouting() {
    val communityService by inject<CommunityService>()

    route("/community") {

        get("/{${Path.COMMUNITY}}") {
            val community = call.getCommunity()

            val serviceName = call.getService()

            val instance = call.getInstance()
            val sort = call.getSort()
            val order = call.getOrder()
            val time = call.getTime()
            val limit = call.getLimit()
            val after = call.getAfter()

            val communityRequest = CommunityRequest(
                call.info,
                community,
                Service(serviceName, instance),
                Filtering(sort, order, time),
                limit,
                after
            )

            val communityResource = communityService.getCommunity(communityRequest)

            call.respondWithResource(communityResource)
        }

        get("/{${Path.COMMUNITY}}/info") {
            val community = call.getCommunity()
            val serviceName = call.getService()
            val instance = call.getInstance()

            val communityInfoRequest = CommunityInfoRequest(call.info, community, Service(serviceName, instance))

            val communityInfo = communityService.getCommunityInfo(communityInfoRequest)

            call.respondWithResource(communityInfo)
        }
    }
}
