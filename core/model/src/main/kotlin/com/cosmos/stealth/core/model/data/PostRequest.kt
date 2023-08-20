package com.cosmos.stealth.core.model.data

import com.cosmos.stealth.core.model.api.Service

data class PostRequest(
    val info: RequestInfo,

    val post: String,

    val service: Service,

    val filtering: Filtering,

    val limit: Int = Default.POST_LIMIT
)
