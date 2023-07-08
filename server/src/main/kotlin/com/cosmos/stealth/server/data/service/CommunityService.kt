package com.cosmos.stealth.server.data.service

import com.cosmos.stealth.core.model.api.AfterKey
import com.cosmos.stealth.core.model.api.Community
import com.cosmos.stealth.core.model.api.CommunityInfo
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.core.model.data.Request
import com.cosmos.stealth.core.model.data.RequestInfo
import com.cosmos.stealth.core.network.util.Resource
import com.cosmos.stealth.services.reddit.RedditGateway
import com.cosmos.stealth.services.teddit.TedditGateway

class CommunityService(
    redditGateway: RedditGateway,
    tedditGateway: TedditGateway
) : BaseService(redditGateway, tedditGateway) {

    suspend fun getCommunity(
        requestInfo: RequestInfo,
        community: String,
        service: Service,
        sort: Sort,
        afterKey: AfterKey?
    ): Resource<Community> {
        return getServiceGateway(service).getCommunity(Request(service, requestInfo), community, sort, afterKey)
    }

    suspend fun getCommunityInfo(
        requestInfo: RequestInfo,
        community: String,
        service: Service
    ): Resource<CommunityInfo> {
        return getServiceGateway(service).getCommunityInfo(Request(service, requestInfo), community)
    }
}
