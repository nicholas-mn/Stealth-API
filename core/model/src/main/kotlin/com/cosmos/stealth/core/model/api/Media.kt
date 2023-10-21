package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Media description
 *
 * @param mime 
 * @param source 
 * @param id 
 * @param resolutions Other resolutions
 * @param alternatives Alternative sources (e.g. with a different type)
 */
@JsonClass(generateAdapter = true)
data class Media (
    @Json(name = "mime")
    val mime: String,

    @Json(name = "source")
    val source: MediaSource,

    @Json(name = "id")
    val id: String? = null,

    /* Other resolutions */
    @Json(name = "resolutions")
    val resolutions: List<MediaSource>? = null,

    /* Alternative sources (e.g. with a different type) */
    @Json(name = "alternatives")
    val alternatives: List<Media>? = null,

    @Json(name = "caption")
    val caption: String? = null,
)
