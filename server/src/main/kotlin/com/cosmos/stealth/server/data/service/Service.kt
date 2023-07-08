package com.cosmos.stealth.server.data.service

import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.services.base.data.ServiceGateway
import com.cosmos.stealth.services.reddit.RedditGateway
import com.cosmos.stealth.services.teddit.TedditGateway

abstract class Service(private val redditGateway: RedditGateway, private val tedditGateway: TedditGateway) {

    protected fun getServiceGateway(service: Service): ServiceGateway {
        return when (service.name) {
            ServiceName.reddit -> redditGateway
            ServiceName.teddit -> tedditGateway
            ServiceName.lemmy -> TODO()
        }
    }
}
