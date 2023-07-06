package com.cosmos.stealth.core.network.util

import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

private val GIF_REGEX = Regex("gif(v)?")

fun getImageIdFromImgurLink(link: String): String {
    return link.toHttpUrlOrNull()?.pathSegments?.getOrNull(0) ?: ""
}

fun getImgurVideo(link: String): String {
    return link.replace(GIF_REGEX, "mp4")
}

fun getUrlFromImgurId(hash: String, extension: String = ".jpeg"): String {
    return "https://i.imgur.com/$hash$extension"
}
