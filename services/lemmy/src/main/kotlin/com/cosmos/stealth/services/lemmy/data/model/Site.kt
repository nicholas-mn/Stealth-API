package com.cosmos.stealth.services.lemmy.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Site(
    @Json(name = "id")
    val id: Int,

    @Json(name = "name")
    val name: String,

    @Json(name = "sidebar")
    val sidebar: String? = null,

    @Json(name = "published")
    val published: String,

    @Json(name = "updated")
    val updated: String? = null,

    @Json(name = "icon")
    val icon: String? = null,

    @Json(name = "banner")
    val banner: String? = null,

    @Json(name = "description")
    val description: String? = null,

    @Json(name = "actor_id")
    val actorId: String,

    @Json(name = "last_refreshed_at")
    val lastRefreshedAt: String,

    @Json(name = "inbox_url")
    val inboxUrl: String,

    @Json(name = "private_key")
    val privateKey: String? = null,

    @Json(name = "public_key")
    val publicKey: String,

    @Json(name = "instance_id")
    val instanceId: Int
)
