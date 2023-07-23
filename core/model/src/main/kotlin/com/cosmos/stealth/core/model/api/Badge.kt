package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Badge description
 *
 * @param badgeDataList
 * @param background 
 */
@JsonClass(generateAdapter = true)
data class Badge (
    @Json(name = "data")
    val badgeDataList: List<BadgeData>,

    @Json(name = "background")
    val background: String? = null
)
