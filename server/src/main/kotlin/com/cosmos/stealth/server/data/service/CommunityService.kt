package com.cosmos.stealth.server.data.service

import com.cosmos.stealth.core.model.api.Community
import com.cosmos.stealth.core.model.api.CommunityInfo
import com.cosmos.stealth.core.model.data.CommunityInfoRequest
import com.cosmos.stealth.core.model.data.CommunityRequest
import com.cosmos.stealth.core.network.util.Resource
import com.cosmos.stealth.server.data.manager.GatewayManager
import org.koin.core.annotation.Single

@Single
class CommunityService(private val gatewayManager: GatewayManager) {

    suspend fun getCommunity(communityRequest: CommunityRequest): Resource<Community> {
        return gatewayManager.getServiceGateway(communityRequest.service).getCommunity(communityRequest)
    }

    suspend fun getCommunityInfo(communityInfoRequest: CommunityInfoRequest): Resource<CommunityInfo> {
        return gatewayManager.getServiceGateway(communityInfoRequest.service).getCommunityInfo(communityInfoRequest)
    }
}
