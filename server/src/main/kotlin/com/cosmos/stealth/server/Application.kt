package com.cosmos.stealth.server

import com.cosmos.stealth.server.data.config.configureContentNegotiation
import com.cosmos.stealth.server.data.config.configureDependencyInjection
import com.cosmos.stealth.server.data.config.configureForwardedHeaders
import com.cosmos.stealth.server.data.config.configureStatusPage
import com.cosmos.stealth.server.data.manager.GatewayManager
import com.cosmos.stealth.server.data.route.configureRouting
import io.ktor.server.application.Application
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.ktor.ext.get

fun main(args: Array<String>) {
    embeddedServer(Netty, commandLineEnvironment(args)).start(wait = true)
}

fun Application.main() {
    configureDependencyInjection()
    configureContentNegotiation()
    configureForwardedHeaders()
    configureStatusPage()
    configureRouting()

    initServices()
}

private fun Application.initServices() { get<GatewayManager>() }
