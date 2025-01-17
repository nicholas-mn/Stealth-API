package com.cosmos.stealth.services.reddit.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MediaPreview (
    @Json(name = "images")
    val images: List<Image>,

    @Json(name = "reddit_video_preview")
    val videoPreview: RedditVideoPreview?
)
