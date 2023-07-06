package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param results
 */
@JsonClass(generateAdapter = true)
data class UserResults (
    @Json(name = "results")
    val results: List<UserInfo>
) : SearchResults(SearchType.user)
