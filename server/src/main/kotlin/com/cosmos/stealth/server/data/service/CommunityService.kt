package com.cosmos.stealth.server.data.service

import com.cosmos.stealth.core.model.api.AfterKey
import com.cosmos.stealth.core.model.api.Community
import com.cosmos.stealth.core.model.api.CommunityInfo
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.core.model.data.Request
import com.cosmos.stealth.core.model.data.RequestInfo
import com.cosmos.stealth.core.network.util.Resource
import com.cosmos.stealth.server.data.manager.GatewayManager

class CommunityService(private val gatewayManager: GatewayManager) {

    suspend fun getCommunity(
        requestInfo: RequestInfo,
        community: String,
        service: Service,
        sort: Sort,
        afterKey: AfterKey?
    ): Resource<Community> {
        return gatewayManager.getServiceGateway(service)
            .getCommunity(Request(service, requestInfo), community, sort, afterKey)
    }

    suspend fun getCommunityInfo(
        requestInfo: RequestInfo,
        community: String,
        service: Service
    ): Resource<CommunityInfo> {
        return gatewayManager.getServiceGateway(service)
            .getCommunityInfo(Request(service, requestInfo), community)
    }
}
