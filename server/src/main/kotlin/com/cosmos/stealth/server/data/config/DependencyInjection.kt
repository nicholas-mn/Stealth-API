package com.cosmos.stealth.server.data.config

import com.cosmos.stealth.core.common.di.DispatchersModule
import com.cosmos.stealth.core.data.di.DataModule
import com.cosmos.stealth.core.network.di.NetworkModule
import com.cosmos.stealth.server.di.ManagerModule
import com.cosmos.stealth.server.di.ServiceModule
import com.cosmos.stealth.services.lemmy.di.LemmyMapperModule
import com.cosmos.stealth.services.lemmy.di.LemmyNetworkModule
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
            DataModule.dataModule,
            RedditMapperModule.redditMapperModule,
            RedditNetworkModule.redditNetworkModule,
            TedditNetworkModule.tedditNetworkModule,
            LemmyNetworkModule.lemmyNetworkModule,
            LemmyMapperModule.lemmyMapperModule,
            ManagerModule.managerModule,
            ServiceModule.serviceModule
        )
    }
}
