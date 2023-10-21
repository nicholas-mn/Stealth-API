package com.cosmos.stealth.services.lemmy.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentAggregates(
    @Json(name = "id")
    val id: Int,

    @Json(name = "comment_id")
    val commentId: Int,

    @Json(name = "score")
    val score: Int,

    @Json(name = "upvotes")
    val upvotes: Int,

    @Json(name = "downvotes")
    val downvotes: Int,

    @Json(name = "published")
    val published: String,

    @Json(name = "child_count")
    val childCount: Int,

    @Json(name = "hot_rank")
    val hotRank: Int
)
