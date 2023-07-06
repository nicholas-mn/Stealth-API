package com.cosmos.stealth.core.model.data

data class RequestInfo(
    val remoteHost: String,

    val proxyMode: Boolean = false
) {
    val host: String?
        get() = if (!proxyMode) remoteHost else null
}
