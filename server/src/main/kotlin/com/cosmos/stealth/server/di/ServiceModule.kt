package com.cosmos.stealth.server.di

import com.cosmos.stealth.server.data.manager.GatewayManager
import com.cosmos.stealth.server.data.service.CommunityService
import com.cosmos.stealth.server.data.service.ContentService
import com.cosmos.stealth.server.data.service.FeedService
import com.cosmos.stealth.server.data.service.PostService
import com.cosmos.stealth.server.data.service.SearchService
import com.cosmos.stealth.server.data.service.ServicesService
import com.cosmos.stealth.server.data.service.UserService
import org.koin.dsl.module

object ServiceModule {

    @Suppress("MemberNameEqualsClassName")
    val serviceModule = module {
        single { provideFeedService(get()) }
        single { provideCommunityService(get()) }
        single { provideUserService(get()) }
        single { providePostService(get()) }
        single { provideContentService(get()) }
        single { provideSearchService(get()) }
        single { provideServicesService() }
    }

    private fun provideFeedService(gatewayManager: GatewayManager): FeedService {
        return FeedService(gatewayManager)
    }

    private fun provideCommunityService(gatewayManager: GatewayManager): CommunityService {
        return CommunityService(gatewayManager)
    }

    private fun provideUserService(gatewayManager: GatewayManager): UserService {
        return UserService(gatewayManager)
    }

    private fun providePostService(gatewayManager: GatewayManager): PostService {
        return PostService(gatewayManager)
    }

    private fun provideContentService(gatewayManager: GatewayManager): ContentService {
        return ContentService(gatewayManager)
    }

    private fun provideSearchService(gatewayManager: GatewayManager): SearchService {
        return SearchService(gatewayManager)
    }

    private fun provideServicesService(): ServicesService {
        return ServicesService()
    }
}
