package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Awards received
 *
 * @param total 
 * @param awards 
 */
@JsonClass(generateAdapter = true)
data class Awards (
    @Json(name = "total")
    val total: Int? = null,

    @Json(name = "awards")
    val awards: List<Award>? = null
)
