package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Source for a Media object
 *
 * @param url 
 * @param width 
 * @param height 
 */
@JsonClass(generateAdapter = true)
data class MediaSource (
    @Json(name = "url")
    val url: String,

    @Json(name = "width")
    val width: Int? = null,

    @Json(name = "height")
    val height: Int? = null
)

