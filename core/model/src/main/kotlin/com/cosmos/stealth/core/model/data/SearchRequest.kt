package com.cosmos.stealth.core.model.data

import com.cosmos.stealth.core.model.api.AfterKey
import com.cosmos.stealth.core.model.api.SearchType
import com.cosmos.stealth.core.model.api.Service

data class SearchRequest(
    val info: RequestInfo,

    val query: String,

    val service: Service,

    val type: SearchType = Default.SEARCH_TYPE,

    val filtering: Filtering,

    val community: String? = null,

    val user: String? = null,

    val limit: Int = Default.LIMIT,

    val afterKey: AfterKey? = null
)
