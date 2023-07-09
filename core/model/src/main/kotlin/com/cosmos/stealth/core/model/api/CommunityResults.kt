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
data class CommunityResults (
    @Json(name = "results")
    val results: List<CommunityInfo>,

    @Json(name = "after")
    val after: AfterKey?
) : SearchResults(SearchType.community)
