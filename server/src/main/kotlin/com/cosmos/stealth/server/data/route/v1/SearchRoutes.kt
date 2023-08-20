package com.cosmos.stealth.server.data.route.v1

import com.cosmos.stealth.core.model.api.Order
import com.cosmos.stealth.core.model.api.SearchType
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.ServiceName
import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.core.model.api.Time
import com.cosmos.stealth.core.model.data.Default
import com.cosmos.stealth.core.model.data.Filtering
import com.cosmos.stealth.core.model.data.SearchRequest
import com.cosmos.stealth.server.data.service.SearchService
import com.cosmos.stealth.server.util.extension.getPath
import com.cosmos.stealth.server.util.extension.getQuery
import com.cosmos.stealth.server.util.extension.info
import com.cosmos.stealth.server.util.extension.respondWithResource
import com.cosmos.stealth.services.base.util.extension.toAfterKey
import io.ktor.server.application.call
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject

fun Route.searchRouting() {
    val searchService by inject<SearchService>()

    get("/search") {
        val query = call.getPath("q") ?: error("Query is required")
        val service = call.getQuery("service") ?: error("Service is required")

        val serviceName = ServiceName.decode(service) ?: error("Unknown service $service")

        val instance = call.getQuery("instance")
        val sort = Sort.decode(call.getQuery("sort")) ?: Default.SORT
        val order = Order.decode(call.getQuery("order")) ?: Default.ORDER
        val time = Time.decode(call.getQuery("time")) ?: Default.TIME
        val community = call.getQuery("community")
        val user = call.getQuery("user")
        val limit = call.getQuery("limit")?.toIntOrNull() ?: Default.LIMIT
        val after = call.getQuery("after")?.toAfterKey()
        val type = SearchType.decode(call.getQuery("type")) ?: Default.SEARCH_TYPE

        val searchRequest = SearchRequest(
            call.info,
            query,
            Service(serviceName, instance),
            type,
            Filtering(sort, order, time),
            community,
            user,
            limit,
            after
        )

        val searchResults = searchService.getSearchResults(searchRequest)

        call.respondWithResource(searchResults)
    }
}
