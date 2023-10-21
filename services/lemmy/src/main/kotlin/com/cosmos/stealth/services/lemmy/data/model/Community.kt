package com.cosmos.stealth.services.lemmy.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Community(
    @Json(name = "id")
    val id: Int,

    @Json(name = "name")
    val name: String,

    @Json(name = "title")
    val title: String,

    @Json(name = "description")
    val description: String? = null,

    @Json(name = "removed")
    val removed: Boolean,

    @Json(name = "published")
    val published: String,

    @Json(name = "updated")
    val updated: String? = null,

    @Json(name = "deleted")
    val deleted: Boolean,

    @Json(name = "nsfw")
    val nsfw: Boolean,

    @Json(name = "actor_id")
    val actorId: String,

    @Json(name = "local")
    val local: Boolean,

    @Json(name = "icon")
    val icon: String? = null,

    @Json(name = "banner")
    val banner: String? = null,

    @Json(name = "followers_url")
    val followersUrl: String?,

    @Json(name = "inbox_url")
    val inboxUrl: String?,

    @Json(name = "hidden")
    val hidden: Boolean,

    @Json(name = "instance_id")
    val instanceId: Int
)
