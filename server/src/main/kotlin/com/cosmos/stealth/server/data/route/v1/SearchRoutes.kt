package com.cosmos.stealth.server.data.route.v1

import com.cosmos.stealth.core.common.util.MessageHandler
import com.cosmos.stealth.core.model.api.SearchType
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.data.Default
import com.cosmos.stealth.core.model.data.Filtering
import com.cosmos.stealth.core.model.data.Query
import com.cosmos.stealth.core.model.data.SearchRequest
import com.cosmos.stealth.server.data.service.SearchService
import com.cosmos.stealth.server.util.extension.getQuery
import com.cosmos.stealth.server.util.extension.info
import com.cosmos.stealth.server.util.extension.respondWithResource
import com.cosmos.stealth.server.util.getAfter
import com.cosmos.stealth.server.util.getInstance
import com.cosmos.stealth.server.util.getLimit
import com.cosmos.stealth.server.util.getOrder
import com.cosmos.stealth.server.util.getService
import com.cosmos.stealth.server.util.getSort
import com.cosmos.stealth.server.util.getTime
import com.cosmos.stealth.server.util.requireQuery
import io.ktor.server.application.call
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject
import java.util.Locale

fun Route.searchRouting() {
    val searchService by inject<SearchService>()

    get("/search") {
        val query = call.requireQuery(Query.Q) {
            MessageHandler.getString(Locale.ENGLISH, "model.error.missing_query")
        }

        val serviceName = call.getService()

        val instance = call.getInstance()
        val sort = call.getSort()
        val order = call.getOrder()
        val time = call.getTime()

        val community = call.getQuery(Query.COMMUNITY)
        val user = call.getQuery(Query.USER)

        val limit = call.getLimit()
        val after = call.getAfter()

        val type = SearchType.decode(call.getQuery(Query.TYPE)) ?: Default.SEARCH_TYPE

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
