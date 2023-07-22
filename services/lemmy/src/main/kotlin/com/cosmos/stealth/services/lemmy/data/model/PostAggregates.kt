package com.cosmos.stealth.services.lemmy.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostAggregates(
    @Json(name = "id")
    val id: Int,

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
    val published: String,

    @Json(name = "newest_comment_time_necro")
    val newestCommentTimeNecro: String,

    @Json(name = "newest_comment_time")
    val newestCommentTime: String,

    @Json(name = "featured_community")
    val featuredCommunity: Boolean,

    @Json(name = "featured_local")
    val featuredLocal: Boolean,

    @Json(name = "hot_rank")
    val hotRank: Int,

    @Json(name = "hot_rank_active")
    val hotRankActive: Int
)
