package com.cosmos.stealth.server.data.config

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.forwardedheaders.ForwardedHeaders
import io.ktor.server.plugins.forwardedheaders.XForwardedHeaders

fun Application.configureForwardedHeaders() {
    install(ForwardedHeaders)
    install(XForwardedHeaders) { useFirstProxy() }
}
