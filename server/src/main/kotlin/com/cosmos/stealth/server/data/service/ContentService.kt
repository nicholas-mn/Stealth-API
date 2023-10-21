package com.cosmos.stealth.server.data.service

import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.data.MoreContentRequest
import com.cosmos.stealth.core.network.util.Resource
import com.cosmos.stealth.server.data.manager.GatewayManager
import org.koin.core.annotation.Single

@Single
class ContentService(private val gatewayManager: GatewayManager) {

    suspend fun getMoreContent(moreContentRequest: MoreContentRequest): Resource<List<Feedable>> {
        val service = moreContentRequest.appendable.service
        return gatewayManager.getServiceGateway(service).getMoreContent(moreContentRequest)
    }
}
