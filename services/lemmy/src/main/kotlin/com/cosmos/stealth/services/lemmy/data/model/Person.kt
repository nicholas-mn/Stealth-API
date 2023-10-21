package com.cosmos.stealth.services.lemmy.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Person(
    @Json(name = "id")
    val id: Int,

    @Json(name = "name")
    val name: String,

    @Json(name = "display_name")
    val displayName: String? = null,

    @Json(name = "avatar")
    val avatar: String? = null,

    @Json(name = "banned")
    val banned: Boolean,

    @Json(name = "published")
    val published: String,

    @Json(name = "updated")
    val updated: String? = null,

    @Json(name = "actor_id")
    val actorId: String,

    @Json(name = "bio")
    val bio: String? = null,

    @Json(name = "local")
    val local: Boolean,

    @Json(name = "banner")
    val banner: String? = null,

    @Json(name = "deleted")
    val deleted: Boolean,

    @Json(name = "inbox_url")
    val inboxUrl: String?,

    @Json(name = "matrix_user_id")
    val matrixUserId: String? = null,

    @Json(name = "admin")
    val admin: Boolean,

    @Json(name = "bot_account")
    val botAccount: Boolean,

    @Json(name = "ban_expires")
    val banExpires: String? = null,

    @Json(name = "instance_id")
    val instanceId: Int
)
