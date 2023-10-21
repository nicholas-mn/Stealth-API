package com.cosmos.stealth.core.model.data

import com.cosmos.stealth.core.model.api.Service

data class CommunityInfoRequest(
    val info: RequestInfo,

    val community: String,

    val service: Service
)
