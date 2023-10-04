package com.cosmos.stealth.core.network.util.extension

import com.cosmos.stealth.core.model.api.Media
import com.cosmos.stealth.core.model.api.MediaSource
import com.cosmos.stealth.core.network.data.model.HostFormat
import io.ktor.http.ContentType
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.UnknownHostException

@Suppress("SwallowedException")
val String.hostFormat: HostFormat
    get() {
        return try {
            when (val inetAddress = InetAddress.getByName(this)) {
                is Inet6Address -> HostFormat.IPV6
                is Inet4Address -> if (inetAddress.hostAddress.equals(this)) HostFormat.IPV4 else HostFormat.UNKNOWN
                else -> HostFormat.UNKNOWN
            }
        } catch (e: UnknownHostException) {
            HostFormat.UNKNOWN
        } catch (e: SecurityException) {
            HostFormat.UNKNOWN
        }
    }

val String.mimeType: ContentType?
    get() = toHttpUrlOrNull()?.mimeType

fun String.toMedia(fallbackType: ContentType? = null): Media? {
    return mimeType
        ?.run { if (this == ContentType.Any) fallbackType else this }
        ?.run { toMedia(this.mime) }
}

fun String.toMedia(mime: String): Media {
    return Media(mime, MediaSource(this))
}
