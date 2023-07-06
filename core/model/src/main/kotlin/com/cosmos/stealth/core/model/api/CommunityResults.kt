package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param results
 */
@JsonClass(generateAdapter = true)
data class CommunityResults (
    @Json(name = "results")
    val results: List<CommunityInfo>
) : SearchResults(SearchType.community)
