package com.cosmos.stealth.core.model.data

import com.cosmos.stealth.core.model.api.Service

data class Request(
    val service: Service,

    val info: RequestInfo
)
