package com.cosmos.stealth.services.lemmy.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostAggregates(
    @Json(name = "post_id")
    val postId: Int,

    @Json(name = "comments")
    val comments: Int,

    @Json(name = "score")
    val score: Int,

    @Json(name = "upvotes")
    val upvotes: Int,

    @Json(name = "downvotes")
    val downvotes: Int,

    @Json(name = "published")
    val published: String
)
