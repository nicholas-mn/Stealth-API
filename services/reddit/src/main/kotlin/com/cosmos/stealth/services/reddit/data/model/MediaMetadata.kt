package com.cosmos.stealth.services.reddit.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class MediaMetadata(
    val items: List<GalleryItem>
)
