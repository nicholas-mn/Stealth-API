package com.cosmos.stealth.services.reddit.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Media (
    @Json(name = "type")
    val type: String?,

    @Json(name = "oembed")
    val embed: Embed?,

    @Json(name = "reddit_video")
    val redditVideoPreview: RedditVideoPreview?
)
