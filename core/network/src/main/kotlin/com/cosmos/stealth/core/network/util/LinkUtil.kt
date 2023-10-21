package com.cosmos.stealth.core.network.util

import com.cosmos.stealth.core.model.api.MediaType
import com.cosmos.stealth.core.network.util.extension.isGif
import com.cosmos.stealth.core.network.util.extension.isImage
import com.cosmos.stealth.core.network.util.extension.isVideo
import com.cosmos.stealth.core.network.util.extension.mimeType
import io.ktor.http.ContentType
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

private val IMGUR_LINK = Regex("([im]\\.)?(stack\\.)?imgur\\.(com|io)")
private val GFYCAT_LINK = Regex("(.+?\\.)?gfycat\\.com")
private val REDGIFS_LINK = Regex("(.+?\\.)?redgifs\\.com")
private val STREAMABLE_LINK = Regex("(.+?)\\.streamable\\.com")

fun getLinkType(link: String): MediaType {
    val httpUrl = link.toHttpUrlOrNull() ?: return MediaType.NO_MEDIA
    val domain = httpUrl.host
    val mime by lazy { httpUrl.mimeType }

    return when {
        domain.matches(IMGUR_LINK) -> getImgurLinkType(link, mime)

        domain.matches(GFYCAT_LINK) -> MediaType.GFYCAT

        domain.matches(REDGIFS_LINK) -> MediaType.REDGIFS

        domain.matches(STREAMABLE_LINK) -> MediaType.STREAMABLE

        mime.isImage -> MediaType.IMAGE

        mime.isVideo -> MediaType.VIDEO

        else -> MediaType.LINK
    }
}

private fun getImgurLinkType(link: String, mime: ContentType?): MediaType {
    return when {
        link.contains("/a/") -> MediaType.IMGUR_ALBUM
        link.contains("/gallery/") -> MediaType.IMGUR_GALLERY
        link.contains(".gifv") -> MediaType.IMGUR_GIF
        mime.isGif -> MediaType.IMGUR_GIF
        mime.isVideo -> MediaType.IMGUR_VIDEO
        mime.isImage -> MediaType.IMGUR_IMAGE
        else -> MediaType.IMGUR_LINK
    }
}
