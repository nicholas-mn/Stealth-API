package com.cosmos.stealth.services.reddit.util.extension

import com.cosmos.stealth.core.model.api.Media
import com.cosmos.stealth.core.network.util.extension.mime
import com.cosmos.stealth.core.network.util.extension.mimeType
import com.cosmos.stealth.services.reddit.data.model.Image

fun Image.toMedia(): Media? {
    val mime = imageSource.url.mimeType?.mime ?: return null

    return Media(mime, imageSource.toMediaSource(), null, resolutions.map { it.toMediaSource() })
}
