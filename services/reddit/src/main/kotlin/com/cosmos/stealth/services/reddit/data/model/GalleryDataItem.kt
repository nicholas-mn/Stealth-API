package com.cosmos.stealth.services.reddit.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GalleryDataItem(
    @Json(name = "caption")
    val caption: String?,
    @Json(name = "media_id")
    val mediaId: String
)
