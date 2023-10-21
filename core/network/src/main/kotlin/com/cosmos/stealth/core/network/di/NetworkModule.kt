package com.cosmos.stealth.core.network.di

import com.cosmos.stealth.core.common.di.DispatchersModule
import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.IO_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.model.adapter.AfterKeyAdapter
import com.cosmos.stealth.core.model.api.Appendable
import com.cosmos.stealth.core.model.api.Commentable
import com.cosmos.stealth.core.model.api.CommunityResults
import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.FeedableResults
import com.cosmos.stealth.core.model.api.FeedableType
import com.cosmos.stealth.core.model.api.Postable
import com.cosmos.stealth.core.model.api.SearchResults
import com.cosmos.stealth.core.model.api.SearchType
import com.cosmos.stealth.core.model.api.UserResults
import com.cosmos.stealth.core.network.data.converter.MoshiContentConverter
import com.cosmos.stealth.core.network.di.NetworkModule.Qualifier.STEALTH_QUALIFIER
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module(includes = [DispatchersModule::class])
@ComponentScan("com.cosmos.stealth.core.network")
class NetworkModule {

    object Qualifier {
        const val STEALTH_QUALIFIER = "stealth"
    }

    @Single
    @Named(STEALTH_QUALIFIER)
    fun provideStealthMoshi(): Moshi {
        // TODO
        return Moshi.Builder()
            .add(
                PolymorphicJsonAdapterFactory.of(Feedable::class.java, "type")
                    .withSubtype(Postable::class.java, FeedableType.post.value)
                    .withSubtype(Commentable::class.java, FeedableType.comment.value)
                    .withSubtype(Appendable::class.java, FeedableType.more.value)
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

    @Single
    @Named(STEALTH_QUALIFIER)
    fun provideStealthMoshiContentConverter(
        @Named(STEALTH_QUALIFIER) moshi: Moshi,
        @Named(IO_DISPATCHER_QUALIFIER) ioDispatcher: CoroutineDispatcher
    ): MoshiContentConverter {
        return MoshiContentConverter(moshi, ioDispatcher)
    }
}
