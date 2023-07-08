package com.cosmos.stealth.server.data.service

import com.cosmos.stealth.core.model.api.AfterKey
import com.cosmos.stealth.core.model.api.SearchResults
import com.cosmos.stealth.core.model.api.SearchType
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.core.model.data.Request
import com.cosmos.stealth.core.model.data.RequestInfo
import com.cosmos.stealth.core.network.util.Resource
import com.cosmos.stealth.services.reddit.RedditGateway
import com.cosmos.stealth.services.teddit.TedditGateway

class SearchService(
    redditGateway: RedditGateway,
    tedditGateway: TedditGateway
) : BaseService(redditGateway, tedditGateway) {

    @Suppress("LongParameterList")
    suspend fun getSearchResults(
        requestInfo: RequestInfo,
        query: String,
        service: Service,
        sort: Sort,
        afterKey: AfterKey?,
        type: SearchType
    ): Resource<SearchResults> {
        return getServiceGateway(service).getSearchResults(Request(service, requestInfo), query, sort, afterKey, type)
    }

    @Suppress("LongParameterList")
    suspend fun getCommunitySearchResults(
        requestInfo: RequestInfo,
        community: String,
        query: String,
        service: Service,
        sort: Sort,
        afterKey: AfterKey?,
        type: SearchType
    ): Resource<SearchResults> {
        return getServiceGateway(service)
            .getCommunitySearchResults(Request(service, requestInfo), community, query, sort, afterKey, type)
    }
}
