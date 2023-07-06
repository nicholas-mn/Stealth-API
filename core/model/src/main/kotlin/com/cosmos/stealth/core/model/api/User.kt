package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Info and feed about a user
 *
 * @param info 
 * @param feed 
 */
@JsonClass(generateAdapter = true)
data class User (
    @Json(name = "info")
    val info: UserInfo,

    @Json(name = "feed")
    val feed: Feed
)
