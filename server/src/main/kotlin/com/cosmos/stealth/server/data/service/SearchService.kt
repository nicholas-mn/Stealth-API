package com.cosmos.stealth.server.data.service

import com.cosmos.stealth.core.model.api.SearchResults
import com.cosmos.stealth.core.model.data.SearchRequest
import com.cosmos.stealth.core.network.util.Resource
import com.cosmos.stealth.server.data.manager.GatewayManager
import org.koin.core.annotation.Single

@Single
class SearchService(private val gatewayManager: GatewayManager) {

    @Suppress("LongParameterList")
    suspend fun getSearchResults(searchRequest: SearchRequest): Resource<SearchResults> {
        return gatewayManager.getServiceGateway(searchRequest.service).getSearchResults(searchRequest)
    }
}
