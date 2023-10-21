package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * An aggregated feed
 *
 * @param items 
 * @param after 
 * @param status 
 */
@JsonClass(generateAdapter = true)
data class Feed (
    @Json(name = "items")
    val items: List<Feedable>,

    @Json(name = "after")
    val after: List<After>? = null,

    @Json(name = "status")
    val status: List<Status>
)
