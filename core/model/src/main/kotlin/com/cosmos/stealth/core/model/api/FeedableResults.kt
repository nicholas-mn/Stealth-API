package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param results
 * @param after
 */
@JsonClass(generateAdapter = true)
data class FeedableResults (
    @Json(name = "results")
    val results: List<Feedable>,

    @Json(name = "after")
    val after: AfterKey?
) : SearchResults(SearchType.feedable)
