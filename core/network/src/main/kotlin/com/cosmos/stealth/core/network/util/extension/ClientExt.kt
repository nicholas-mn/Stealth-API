package com.cosmos.stealth.core.network.util.extension

import com.cosmos.stealth.core.network.data.model.HostFormat
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder

fun HttpMessageBuilder.forward(remoteHost: String?, proxyMode: Boolean = false) {
    if (proxyMode) return

    val forValue = when (remoteHost?.hostFormat) {
        HostFormat.IPV4 -> remoteHost
        HostFormat.IPV6 -> "\"[$remoteHost]\""
        else -> return
    }

    header(HttpHeaders.Forwarded, "for=$forValue")
    header(HttpHeaders.XForwardedFor, remoteHost)
    header("X-Real-IP", remoteHost)
}
