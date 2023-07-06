package com.cosmos.stealth.services.reddit.util

import com.cosmos.stealth.core.model.api.Media
import com.cosmos.stealth.core.model.api.MediaSource
import com.cosmos.stealth.core.network.util.extension.mime
import com.cosmos.stealth.core.network.util.extension.mimeType
import com.cosmos.stealth.services.reddit.data.model.GalleryDataItem
import com.cosmos.stealth.services.reddit.data.model.GalleryItem

fun toMedia(galleryDataItem: GalleryDataItem, galleryItem: GalleryItem): Media? {
    val image = galleryItem.image

    val mediaUrl = image?.mp4 ?: image?.url
    val mime = mediaUrl?.mimeType?.mime

    if (mediaUrl == null || mime == null) return null

    return Media(
        mime,
        MediaSource(mediaUrl, image?.width, image?.height),
        galleryDataItem.mediaId,
        caption = galleryDataItem.caption
    )
}
