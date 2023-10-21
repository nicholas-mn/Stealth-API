package com.cosmos.stealth.core.model.data

import com.cosmos.stealth.core.model.api.Service

data class UserInfoRequest(
    val info: RequestInfo,

    val user: String,

    val service: Service
)
