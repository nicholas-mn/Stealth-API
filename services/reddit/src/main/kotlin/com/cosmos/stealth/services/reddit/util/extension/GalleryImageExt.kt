package com.cosmos.stealth.services.reddit.util.extension

import com.cosmos.stealth.core.model.api.MediaSource
import com.cosmos.stealth.services.reddit.data.model.GalleryImage

fun GalleryImage.toMediaSource(): MediaSource? {
    val url = url ?: mp4 ?: return null
    return MediaSource(url, width, height)
}
