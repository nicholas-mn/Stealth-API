package com.cosmos.stealth.services.reddit.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AboutUserData(
    @Json(name = "is_suspended")
    val isSuspended: Boolean = false,

    @Json(name = "is_employee")
    val isEmployee: Boolean = false,

    @Json(name = "subreddit")
    val subreddit: Subreddit?,

    @Json(name = "id")
    val id: String?,

    @Json(name = "icon_img")
    val iconImg: String?,

    @Json(name = "link_karma")
    val linkKarma: Int = -1,

    @Json(name = "total_karma")
    val totalKarma: Int = -1,

    @Json(name = "name")
    val name: String,

    @Json(name = "created_utc")
    val created: Long = -1,

    @Json(name = "snoovatar_img")
    val snoovatarImg: String?,

    @Json(name = "comment_karma")
    val commentKarma: Int = -1
)
