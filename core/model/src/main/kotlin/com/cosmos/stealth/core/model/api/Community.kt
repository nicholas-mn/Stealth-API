package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Info and feed about a community
 *
 * @param info 
 * @param feed 
 */
@JsonClass(generateAdapter = true)
data class Community (
    @Json(name = "info")
    val info: CommunityInfo,

    @Json(name = "feed")
    val feed: Feed
)
