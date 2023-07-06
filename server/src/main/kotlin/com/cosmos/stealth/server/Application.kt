package com.cosmos.stealth.server

import com.cosmos.stealth.core.common.di.DispatchersModule.dispatchersModule
import com.cosmos.stealth.core.network.di.NetworkModule.networkModule
import com.cosmos.stealth.server.plugins.configureRouting
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.forwardedheaders.ForwardedHeaders
import io.ktor.server.plugins.forwardedheaders.XForwardedHeaders
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    embeddedServer(Netty, commandLineEnvironment(args)).start(wait = true)
}

fun Application.main() {
    install(Koin) {
        modules(dispatchersModule, networkModule)
    }

    install(ForwardedHeaders)
    install(XForwardedHeaders) { useFirstProxy() }

    configureRouting()
}
