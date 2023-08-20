package com.cosmos.stealth.core.model.data

import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.Sort

data class PostRequest(
    val info: RequestInfo,

    val post: String,

    val service: Service,

    val sort: Sort = Default.SORT,

    val limit: Int = Default.POST_LIMIT
)
