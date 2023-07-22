package com.cosmos.stealth.services.lemmy.di

import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.DEFAULT_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.common.util.MarkdownParser
import com.cosmos.stealth.services.lemmy.data.mapper.CommentMapper
import com.cosmos.stealth.services.lemmy.data.mapper.CommunityMapper
import com.cosmos.stealth.services.lemmy.data.mapper.PostMapper
import com.cosmos.stealth.services.lemmy.data.mapper.UserMapper
import com.cosmos.stealth.services.lemmy.di.LemmyMapperModule.Qualifier.LEMMY_QUALIFIER
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.qualifier.named
import org.koin.dsl.module

object LemmyMapperModule {

    object Qualifier {
        val LEMMY_QUALIFIER = named("lemmy")
    }

    @Suppress("MemberNameEqualsClassName")
    val lemmyMapperModule = module {
        single(LEMMY_QUALIFIER) { provideMarkdownParser(get(DEFAULT_DISPATCHER_QUALIFIER)) }
        single { provideCommentMapper(get(LEMMY_QUALIFIER), get(DEFAULT_DISPATCHER_QUALIFIER)) }
        single { provideCommunityMapper(get(LEMMY_QUALIFIER), get(DEFAULT_DISPATCHER_QUALIFIER)) }
        single { providePostMapper(get(LEMMY_QUALIFIER), get(DEFAULT_DISPATCHER_QUALIFIER)) }
        single { provideUserMapper(get(LEMMY_QUALIFIER), get(DEFAULT_DISPATCHER_QUALIFIER)) }
    }

    private fun provideMarkdownParser(defaultDispatcher: CoroutineDispatcher): MarkdownParser {
        return MarkdownParser(defaultDispatcher = defaultDispatcher)
    }

    private fun provideCommentMapper(
        markdownParser: MarkdownParser,
        defaultDispatcher: CoroutineDispatcher
    ): CommentMapper {
        return CommentMapper(markdownParser, defaultDispatcher)
    }

    private fun provideCommunityMapper(
        markdownParser: MarkdownParser,
        defaultDispatcher: CoroutineDispatcher
    ): CommunityMapper {
        return CommunityMapper(markdownParser, defaultDispatcher)
    }

    private fun providePostMapper(
        markdownParser: MarkdownParser,
        defaultDispatcher: CoroutineDispatcher
    ): PostMapper {
        return PostMapper(markdownParser, defaultDispatcher)
    }

    private fun provideUserMapper(
        markdownParser: MarkdownParser,
        defaultDispatcher: CoroutineDispatcher
    ): UserMapper {
        return UserMapper(markdownParser, defaultDispatcher)
    }
}
