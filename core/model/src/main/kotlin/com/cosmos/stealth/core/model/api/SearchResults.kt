package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json

/**
 * 
 *
 * @param type 
 */
sealed class SearchResults(
    @Json(name = "type")
    val type: SearchType
)
