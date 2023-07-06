package com.cosmos.stealth.services.reddit.util.extension

import com.cosmos.stealth.core.model.api.Media
import com.cosmos.stealth.core.model.api.MediaSource
import com.cosmos.stealth.core.network.util.extension.mime
import com.cosmos.stealth.core.network.util.extension.mimeType
import com.cosmos.stealth.services.reddit.data.model.GalleryItem

fun GalleryItem.toMedia(): Media? {
    val mime = mimeType
        ?: image?.url?.mimeType?.mime
        ?: image?.mp4?.mimeType?.mime

    val url = image?.url ?: image?.mp4

    if (mime == null || url == null) return null

    return Media(mime, MediaSource(url, image?.width, image?.height), null, previews?.mapNotNull { it.toMediaSource() })
}