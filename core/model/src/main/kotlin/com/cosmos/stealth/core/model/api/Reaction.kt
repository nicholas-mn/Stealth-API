package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Reaction description
 *
 * @param count 
 * @param media
 * @param name 
 * @param description 
 */
@JsonClass(generateAdapter = true)
data class Reaction (
    @Json(name = "count")
    val count: Int,

    @Json(name = "media")
    val media: Media? = null,

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "description")
    val description: String? = null
)
