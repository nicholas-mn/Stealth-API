package com.cosmos.stealth.server.data.config

import com.cosmos.stealth.core.common.di.ConfigModule
import com.cosmos.stealth.core.network.di.NetworkModule
import com.cosmos.stealth.server.di.ServiceModule
import com.cosmos.stealth.server.util.extension.config
import com.cosmos.stealth.services.lemmy.di.LemmyModule
import com.cosmos.stealth.services.reddit.di.RedditModule
import com.cosmos.stealth.services.teddit.di.TedditModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ksp.generated.module
import org.koin.ktor.plugin.Koin

fun Application.configureDependencyInjection() {
    install(Koin) {
        modules(
            ConfigModule(config).module,
            NetworkModule().module,
            RedditModule().module,
            TedditModule().module,
            LemmyModule().module,
            ServiceModule().module
        )
    }
}
