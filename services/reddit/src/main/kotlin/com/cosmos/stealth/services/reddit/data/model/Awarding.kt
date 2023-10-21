package com.cosmos.stealth.services.reddit.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Awarding (
    @Json(name = "icon_url")
    val url: String,

    @Json(name = "resized_icons")
    val resizedIcons: List<ImageSource>,

    @Json(name = "count")
    val count: Int
)
