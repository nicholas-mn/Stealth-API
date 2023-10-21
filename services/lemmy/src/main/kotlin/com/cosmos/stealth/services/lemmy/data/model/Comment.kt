package com.cosmos.stealth.services.lemmy.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Comment(
    @Json(name = "id")
    val id: Int,

    @Json(name = "creator_id")
    val creatorId: Int,

    @Json(name = "post_id")
    val postId: Int,

    @Json(name = "content")
    val content: String,

    @Json(name = "removed")
    val removed: Boolean,

    @Json(name = "published")
    val published: String,

    @Json(name = "updated")
    val updated: String? = null,

    @Json(name = "deleted")
    val deleted: Boolean,

    @Json(name = "ap_id")
    val apId: String,

    @Json(name = "local")
    val local: Boolean,

    @Json(name = "path")
    val path: String,

    @Json(name = "distinguished")
    val distinguished: Boolean,

    @Json(name = "language_id")
    val languageId: Int
)
