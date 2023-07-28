package com.cosmos.stealth.core.model.data

import com.cosmos.stealth.core.model.api.Appendable

data class MoreContentRequest(
    val info: RequestInfo,

    val appendable: Appendable
)
