package com.cosmos.stealth.services.reddit.util.extension

import com.cosmos.stealth.core.model.api.Media
import com.cosmos.stealth.core.network.util.extension.mime
import com.cosmos.stealth.core.network.util.extension.mimeType
import com.cosmos.stealth.services.reddit.data.model.Image
import io.ktor.http.ContentType
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

fun Image.toMedia(): Media? {
    val mime = imageSource.url.toHttpUrlOrNull()?.run {
        val format = queryParameter("format").orEmpty()

        when {
            format == "mp4" -> ContentType.Video.MP4.mime
            format.startsWith("png") -> ContentType.Image.PNG.mime
            else -> mimeType?.mime
        }
    } ?: return null

    return toMedia(mime)
}

fun Image.toMedia(mime: String): Media {
    return Media(mime, imageSource.toMediaSource(), null, resolutions.map { it.toMediaSource() })
}
