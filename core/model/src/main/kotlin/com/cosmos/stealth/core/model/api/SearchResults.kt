package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param type 
 */
@JsonClass(generateAdapter = true)
sealed class SearchResults(
    @Json(name = "type")
    val type: SearchType
)
