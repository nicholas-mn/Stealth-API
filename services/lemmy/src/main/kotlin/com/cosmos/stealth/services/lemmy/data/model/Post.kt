package com.cosmos.stealth.services.lemmy.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Post(
    @Json(name = "id")
    val id: Int,

    @Json(name = "name")
    val name: String,

    @Json(name = "url")
    val url: String? = null,

    @Json(name = "body")
    val body: String? = null,

    @Json(name = "creator_id")
    val creatorId: Int,

    @Json(name = "community_id")
    val communityId: Int,

    @Json(name = "removed")
    val removed: Boolean,

    @Json(name = "locked")
    val locked: Boolean,

    @Json(name = "published")
    val published: String,

    @Json(name = "updated")
    val updated: String? = null,

    @Json(name = "deleted")
    val deleted: Boolean,

    @Json(name = "nsfw")
    val nsfw: Boolean,

    @Json(name = "embed_title")
    val embedTitle: String? = null,

    @Json(name = "embed_description")
    val embedDescription: String? = null,

    @Json(name = "thumbnail_url")
    val thumbnailUrl: String? = null,

    @Json(name = "ap_id")
    val apId: String,

    @Json(name = "local")
    val local: Boolean,

    @Json(name = "embed_video_url")
    val embedVideoUrl: String? = null,

    @Json(name = "language_id")
    val languageId: Int,

    @Json(name = "featured_community")
    val featuredCommunity: Boolean,

    @Json(name = "featured_local")
    val featuredLocal: Boolean,
)
