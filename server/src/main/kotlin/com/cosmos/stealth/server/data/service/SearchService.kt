package com.cosmos.stealth.server.data.service

import com.cosmos.stealth.core.model.api.SearchResults
import com.cosmos.stealth.core.model.data.SearchRequest
import com.cosmos.stealth.core.network.util.Resource
import com.cosmos.stealth.services.reddit.RedditGateway
import com.cosmos.stealth.services.teddit.TedditGateway

class SearchService(
    redditGateway: RedditGateway,
    tedditGateway: TedditGateway
) : BaseService(redditGateway, tedditGateway) {

    @Suppress("LongParameterList")
    suspend fun getSearchResults(searchRequest: SearchRequest): Resource<SearchResults> {
        return getServiceGateway(searchRequest.service).getSearchResults(searchRequest)
    }
}
