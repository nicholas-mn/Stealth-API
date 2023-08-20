package com.cosmos.stealth.core.model.api

import com.cosmos.stealth.core.model.data.Default
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Request for an aggregated feed
 *
 * @param services 
 * @param after 
 * @param sort 
 */
@JsonClass(generateAdapter = true)
data class FeedRequest (
    @Json(name = "services")
    val services: List<ServiceRequest>,

    @Json(name = "after")
    val after: List<After>? = null,

    @Json(name = "sort")
    val sort: Sort? = Default.SORT,

    @Json(name = "limit")
    val limit: Int? = Default.LIMIT
)
