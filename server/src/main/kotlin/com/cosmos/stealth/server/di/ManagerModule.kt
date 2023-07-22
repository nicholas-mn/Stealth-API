package com.cosmos.stealth.server.di

import com.cosmos.stealth.server.data.manager.GatewayManager
import com.cosmos.stealth.services.lemmy.LemmyGateway
import com.cosmos.stealth.services.reddit.RedditGateway
import com.cosmos.stealth.services.teddit.TedditGateway
import org.koin.dsl.module

object ManagerModule {

    @Suppress("MemberNameEqualsClassName")
    val managerModule = module {
        single { provideGatewayManager(get(), get(), get()) }
    }

    private fun provideGatewayManager(
        redditGateway: RedditGateway,
        tedditGateway: TedditGateway,
        lemmyGateway: LemmyGateway
    ): GatewayManager {
        return GatewayManager(redditGateway, tedditGateway, lemmyGateway)
    }
}
