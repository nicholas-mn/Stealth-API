package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Flair description
 *
 * @param flairDataList
 * @param background 
 */
@JsonClass(generateAdapter = true)
data class Flair (
    @Json(name = "data")
    val flairDataList: List<FlairData>,

    @Json(name = "background")
    val background: String? = null
)
