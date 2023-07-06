package com.cosmos.stealth.core.network.di

import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.DEFAULT_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.IO_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.model.adapter.AfterKeyAdapter
import com.cosmos.stealth.core.model.api.CommentFeedable
import com.cosmos.stealth.core.model.api.CommunityResults
import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.FeedableResults
import com.cosmos.stealth.core.model.api.FeedableType
import com.cosmos.stealth.core.model.api.MoreContentFeedable
import com.cosmos.stealth.core.model.api.PostFeedable
import com.cosmos.stealth.core.model.api.SearchResults
import com.cosmos.stealth.core.model.api.SearchType
import com.cosmos.stealth.core.model.api.UserResults
import com.cosmos.stealth.core.network.data.converter.MoshiContentConverter
import com.cosmos.stealth.core.network.di.NetworkModule.Qualifier.STEALTH_QUALIFIER
import com.cosmos.stealth.core.network.util.UrlSubstitutor
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.qualifier.named
import org.koin.dsl.module

object NetworkModule {

    object Qualifier {
        val STEALTH_QUALIFIER = named("stealth")
    }

    @Suppress("MemberNameEqualsClassName")
    val networkModule = module {
        single { provideUrlSubstitutor(get(DEFAULT_DISPATCHER_QUALIFIER)) }

        single(STEALTH_QUALIFIER) { provideStealthMoshi() }
        single(STEALTH_QUALIFIER) {
            provideStealthMoshiContentConverter(
                get(STEALTH_QUALIFIER),
                get(IO_DISPATCHER_QUALIFIER)
            )
        }
    }

    private fun provideStealthMoshi(): Moshi {
        // TODO
        return Moshi.Builder()
            .add(
                PolymorphicJsonAdapterFactory.of(Feedable::class.java, "type")
                    .withSubtype(PostFeedable::class.java, FeedableType.post.value)
                    .withSubtype(CommentFeedable::class.java, FeedableType.comment.value)
                    .withSubtype(MoreContentFeedable::class.java, FeedableType.more.value)
            )
            .add(
                PolymorphicJsonAdapterFactory.of(SearchResults::class.java, "type")
                    .withSubtype(FeedableResults::class.java, SearchType.feedable.value)
                    .withSubtype(CommunityResults::class.java, SearchType.community.value)
                    .withSubtype(UserResults::class.java, SearchType.user.value)
            )
            .add(AfterKeyAdapter())
            .build()
    }

    private fun provideStealthMoshiContentConverter(
        moshi: Moshi,
        ioDispatcher: CoroutineDispatcher
    ): MoshiContentConverter {
        return MoshiContentConverter(moshi, ioDispatcher)
    }

    private fun provideUrlSubstitutor(defaultDispatcher: CoroutineDispatcher): UrlSubstitutor {
        return UrlSubstitutor(defaultDispatcher)
    }
}
