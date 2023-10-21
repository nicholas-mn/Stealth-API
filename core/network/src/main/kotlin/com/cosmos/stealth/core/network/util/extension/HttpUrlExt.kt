package com.cosmos.stealth.core.network.util.extension

import io.ktor.http.BadContentTypeFormatException
import io.ktor.http.ContentType
import okhttp3.HttpUrl
import java.net.URLConnection

@Suppress("SwallowedException")
val HttpUrl.mimeType: ContentType?
    get() {
        val fileName = pathSegments.lastOrNull().orEmpty()
        val contentType = URLConnection.guessContentTypeFromName(fileName).orEmpty()
        return try {
            ContentType.parse(contentType)
        } catch (e: BadContentTypeFormatException) {
            null
        }
    }
