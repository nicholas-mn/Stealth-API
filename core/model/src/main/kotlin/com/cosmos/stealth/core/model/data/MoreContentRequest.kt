package com.cosmos.stealth.core.model.data

import com.cosmos.stealth.core.model.api.MoreContentFeedable

data class MoreContentRequest(
    val info: RequestInfo,

    val moreContentFeedable: MoreContentFeedable
)
