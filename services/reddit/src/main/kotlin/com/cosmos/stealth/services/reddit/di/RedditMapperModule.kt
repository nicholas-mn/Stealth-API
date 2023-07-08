package com.cosmos.stealth.services.reddit.di

import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.DEFAULT_DISPATCHER_QUALIFIER
import com.cosmos.stealth.services.reddit.data.mapper.CommentMapper
import com.cosmos.stealth.services.reddit.data.mapper.CommunityMapper
import com.cosmos.stealth.services.reddit.data.mapper.PostMapper
import com.cosmos.stealth.services.reddit.data.mapper.UserMapper
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.dsl.module

object RedditMapperModule {

    @Suppress("MemberNameEqualsClassName")
    val redditMapperModule = module {
        single { provideCommentMapper(get(DEFAULT_DISPATCHER_QUALIFIER)) }
        single { provideCommunityMapper(get(DEFAULT_DISPATCHER_QUALIFIER)) }
        single { providePostMapper(get(DEFAULT_DISPATCHER_QUALIFIER)) }
        single { provideUserMapper(get(DEFAULT_DISPATCHER_QUALIFIER)) }
    }

    private fun provideCommentMapper(defaultDispatcher: CoroutineDispatcher): CommentMapper {
        return CommentMapper(defaultDispatcher)
    }

    private fun provideCommunityMapper(defaultDispatcher: CoroutineDispatcher): CommunityMapper {
        return CommunityMapper(defaultDispatcher)
    }

    private fun providePostMapper(defaultDispatcher: CoroutineDispatcher): PostMapper {
        return PostMapper(defaultDispatcher)
    }

    private fun provideUserMapper(defaultDispatcher: CoroutineDispatcher): UserMapper {
        return UserMapper(defaultDispatcher)
    }
}
