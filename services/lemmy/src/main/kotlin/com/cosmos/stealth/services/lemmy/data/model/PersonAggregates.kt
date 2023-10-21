package com.cosmos.stealth.services.lemmy.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PersonAggregates(
    @Json(name = "id")
    val id: Int,

    @Json(name = "person_id")
    val personId: Int,

    @Json(name = "post_count")
    val postCount: Int,

    @Json(name = "post_score")
    val postScore: Int,

    @Json(name = "comment_count")
    val commentCount: Int,

    @Json(name = "comment_score")
    val commentScore: Int
)
