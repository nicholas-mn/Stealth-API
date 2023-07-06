package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Info about a user
 *
 * @param type
 * @param service
 * @param id
 * @param name
 * @param created
 * @param icon
 * @param description
 * @param subscribers
 * @param subscribees
 * @param nsfw
 * @param postCount
 * @param commentCount
 */
@JsonClass(generateAdapter = true)
data class UserInfo (
    @Json(name = "type")
    val type: UserType,

    @Json(name = "service")
    val service: Service,

    @Json(name = "id")
    val id: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "created")
    val created: Long,

    @Json(name = "icon")
    val icon: Media? = null,

    @Json(name = "description")
    val description: String? = null,

    @Json(name = "subscribers")
    val subscribers: Int? = null,

    @Json(name = "subscribees")
    val subscribees: Int? = null,

    @Json(name = "nsfw")
    val nsfw: Boolean? = null,

    @Json(name = "postCount")
    val postCount: Int? = null,

    @Json(name = "commentCount")
    val commentCount: Int? = null,

    @Json(name = "score")
    val score: Int? = null
)
