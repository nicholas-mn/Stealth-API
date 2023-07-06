package com.cosmos.stealth.services.reddit.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AboutData(
    @Json(name = "name")
    val name: String,

    @Json(name = "wiki_enabled")
    val wikiEnabled: Boolean?,

    @Json(name = "display_name")
    val displayName: String,

    @Json(name = "header_img")
    val headerImg: String?,

    @Json(name = "title")
    val title: String,

    @Json(name = "primary_color")
    val primaryColor: String?,

    @Json(name = "active_user_count")
    val activeUserCount: Int?,

    @Json(name = "icon_img")
    val iconImg: String?,

    @Json(name = "subscribers")
    val subscribers: Int?,

    @Json(name = "quarantine")
    val quarantine: Boolean?,

    @Json(name = "public_description_html")
    val publicDescriptionHtml: String?,

    @Json(name = "community_icon")
    val communityIcon: String,

    @Json(name = "banner_background_image")
    val bannerBackgroundImage: String,

    @Json(name = "key_color")
    val keyColor: String?,

    @Json(name = "banner_background_color")
    val bannerBackgroundColor: String?,

    @Json(name = "over18")
    val over18: Boolean?,

    @Json(name = "description_html")
    val descriptionHtml: String?,

    @Json(name = "url")
    val url: String,

    @Json(name = "created_utc")
    val created: Long
)
