package com.cosmos.stealth.server.data.manager

import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.services.base.data.ServiceGateway
import com.cosmos.stealth.services.lemmy.LemmyGateway
import com.cosmos.stealth.services.reddit.RedditGateway
import com.cosmos.stealth.services.teddit.TedditGateway

class GatewayManager(
    private val redditGateway: RedditGateway,
    private val tedditGateway: TedditGateway,
    private val lemmyGateway: LemmyGateway
) {

    fun getServiceGateway(service: Service): ServiceGateway {
        return when (service.name) {
            ServiceName.reddit -> redditGateway
            ServiceName.teddit -> tedditGateway
            ServiceName.lemmy -> lemmyGateway
        }
    }
}
