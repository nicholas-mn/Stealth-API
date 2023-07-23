package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Reaction description
 *
 * @param count 
 * @param source 
 * @param name 
 * @param description 
 * @param resolutions Other resolutions
 */
@JsonClass(generateAdapter = true)
data class Reaction (
    @Json(name = "count")
    val count: Int,

    @Json(name = "source")
    val source: MediaSource,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "description")
    val description: String? = null,

    /* Other resolutions */
    @Json(name = "resolutions")
    val resolutions: List<MediaSource>? = null
)
