package com.cosmos.stealth.services.lemmy.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommunityAggregates(
    @Json(name = "community_id")
    val communityId: Int,

    @Json(name = "subscribers")
    val subscribers: Int,

    @Json(name = "posts")
    val posts: Int,

    @Json(name = "comments")
    val comments: Int,

    @Json(name = "published")
    val published: String,

    @Json(name = "users_active_day")
    val usersActiveDay: Int,

    @Json(name = "users_active_week")
    val usersActiveWeek: Int,

    @Json(name = "users_active_month")
    val usersActiveMonth: Int,

    @Json(name = "users_active_half_year")
    val usersActiveHalfYear: Int
)
