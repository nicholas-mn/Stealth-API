package com.cosmos.stealth.server.di

import com.cosmos.stealth.server.data.service.CommunityService
import com.cosmos.stealth.server.data.service.FeedService
import com.cosmos.stealth.server.data.service.PostService
import com.cosmos.stealth.server.data.service.ServicesService
import com.cosmos.stealth.server.data.service.UserService
import com.cosmos.stealth.services.reddit.RedditGateway
import com.cosmos.stealth.services.teddit.TedditGateway
import org.koin.dsl.module

object ServiceModule {

    @Suppress("MemberNameEqualsClassName")
    val serviceModule = module {
        single { provideFeedService(get(), get()) }
        single { provideCommunityService(get(), get()) }
        single { provideUserService(get(), get()) }
        single { providePostService(get(), get()) }
        single { provideServicesService() }
    }

    private fun provideFeedService(redditGateway: RedditGateway, tedditGateway: TedditGateway): FeedService {
        return FeedService(redditGateway, tedditGateway)
    }

    private fun provideCommunityService(redditGateway: RedditGateway, tedditGateway: TedditGateway): CommunityService {
        return CommunityService(redditGateway, tedditGateway)
    }

    private fun provideUserService(redditGateway: RedditGateway, tedditGateway: TedditGateway): UserService {
        return UserService(redditGateway, tedditGateway)
    }

    private fun providePostService(redditGateway: RedditGateway, tedditGateway: TedditGateway): PostService {
        return PostService(redditGateway, tedditGateway)
    }

    private fun provideServicesService(): ServicesService {
        return ServicesService()
    }
}
