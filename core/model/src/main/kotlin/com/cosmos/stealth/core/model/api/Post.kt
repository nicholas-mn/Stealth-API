package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Post and replies
 *
 * @param post 
 * @param replies 
 */
@JsonClass(generateAdapter = true)
data class Post (
    @Json(name = "post")
    val post: Feedable,

    @Json(name = "replies")
    val replies: List<Feedable>
)

