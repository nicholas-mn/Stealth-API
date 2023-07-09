package com.cosmos.stealth.core.model.data

import com.cosmos.stealth.core.model.api.AfterKey
import com.cosmos.stealth.core.model.api.SearchType
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.Sort

data class SearchRequest(
    val info: RequestInfo,

    val query: String,

    val service: Service,

    val type: SearchType = SearchType.feedable,

    val community: String? = null,

    val user: String? = null,

    val sort: Sort = Sort.best,

    val afterKey: AfterKey? = null
)
