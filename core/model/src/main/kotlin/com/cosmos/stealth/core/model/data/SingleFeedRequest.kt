package com.cosmos.stealth.core.model.data

import com.cosmos.stealth.core.model.api.AfterKey
import com.cosmos.stealth.core.model.api.Service

data class SingleFeedRequest(
    val info: RequestInfo,

    val communities: List<String>,

    val service: Service,

    val filtering: Filtering,

    val limit: Int = Default.LIMIT,

    val afterKey: AfterKey? = null
)
