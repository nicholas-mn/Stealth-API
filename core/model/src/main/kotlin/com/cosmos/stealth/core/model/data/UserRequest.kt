package com.cosmos.stealth.core.model.data

import com.cosmos.stealth.core.model.api.AfterKey
import com.cosmos.stealth.core.model.api.FeedableType
import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.Sort

data class UserRequest(
    val info: RequestInfo,

    val user: String,

    val service: Service,

    val sort: Sort = Default.SORT,

    val limit: Int = Default.LIMIT,

    val afterKey: AfterKey? = null,

    val type: FeedableType = Default.USER_FEEDABLE_TYPE
)
