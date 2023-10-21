package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Info about a community
 *
 * @param type
 * @param service
 * @param id
 * @param name
 * @param created
 * @param title
 * @param shortDescription
 * @param description
 * @param icon
 * @param header
 * @param members
 * @param active
 * @param refLink
 * @param nsfw
 * @param color
 */
@JsonClass(generateAdapter = true)
data class CommunityInfo (
    @Json(name = "type")
    val type: CommunityType,

    @Json(name = "service")
    val service: Service,

    @Json(name = "id")
    val id: String,

    @Json(name = "name")
    val name: String,

    @Json(name = "created")
    val created: Long,

    @Json(name = "title")
    val title: String? = null,

    @Json(name = "shortDescription")
    val shortDescription: String? = null,

    @Json(name = "description")
    val description: String? = null,

    @Json(name = "icon")
    val icon: Media? = null,

    @Json(name = "header")
    val header: Media? = null,

    @Json(name = "members")
    val members: Int? = null,

    @Json(name = "active")
    val active: Int? = null,

    @Json(name = "refLink")
    val refLink: String? = null,

    @Json(name = "nsfw")
    val nsfw: Boolean? = null,

    @Json(name = "color")
    val color: String? = null
)
