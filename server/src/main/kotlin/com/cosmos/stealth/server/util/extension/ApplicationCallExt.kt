package com.cosmos.stealth.server.util.extension

import com.cosmos.stealth.core.model.data.Headers
import com.cosmos.stealth.core.model.data.RequestInfo
import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.origin

val ApplicationCall.info: RequestInfo
    get() {
        val proxyMode = request.headers[Headers.X_PROXY_MODE].toBoolean()
        val remoteHost = request.origin.remoteHost

        return RequestInfo(remoteHost, proxyMode)
    }
