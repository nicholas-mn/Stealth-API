package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Reactions received
 *
 * @param total 
 * @param reactions
 */
@JsonClass(generateAdapter = true)
data class Reactions (
    @Json(name = "total")
    val total: Int? = null,

    @Json(name = "reactions")
    val reactions: List<Reaction>? = null
)
