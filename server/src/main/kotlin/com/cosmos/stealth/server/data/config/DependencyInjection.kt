package com.cosmos.stealth.server.data.config

import com.cosmos.stealth.core.common.di.DispatchersModule
import com.cosmos.stealth.core.network.di.NetworkModule
import com.cosmos.stealth.server.di.ServiceModule
import com.cosmos.stealth.services.reddit.di.RedditMapperModule
import com.cosmos.stealth.services.reddit.di.RedditNetworkModule
import com.cosmos.stealth.services.teddit.di.TedditNetworkModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin

fun Application.configureDependencyInjection() {
    install(Koin) {
        modules(
            DispatchersModule.dispatchersModule,
            NetworkModule.networkModule,
            RedditMapperModule.redditMapperModule,
            RedditNetworkModule.redditNetworkModule,
            TedditNetworkModule.tedditNetworkModule,
            ServiceModule.serviceModule
        )
    }
}
