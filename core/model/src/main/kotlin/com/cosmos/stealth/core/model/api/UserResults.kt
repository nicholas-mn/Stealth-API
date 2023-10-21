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
data class UserResults (
    @Json(name = "results")
    val results: List<UserInfo>,

    @Json(name = "after")
    val after: AfterKey?
) : SearchResults(SearchType.user)
