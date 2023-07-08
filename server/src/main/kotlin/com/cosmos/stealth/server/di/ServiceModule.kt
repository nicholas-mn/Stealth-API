package com.cosmos.stealth.server.di

import com.cosmos.stealth.server.data.service.FeedService
import com.cosmos.stealth.services.reddit.RedditGateway
import com.cosmos.stealth.services.teddit.TedditGateway
import org.koin.dsl.module

object ServiceModule {

    @Suppress("MemberNameEqualsClassName")
    val serviceModule = module {
        single { provideFeedService(get(), get()) }
    }

    private fun provideFeedService(redditGateway: RedditGateway, tedditGateway: TedditGateway): FeedService {
        return FeedService(redditGateway, tedditGateway)
    }
}
