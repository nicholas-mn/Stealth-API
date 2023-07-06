package com.cosmos.stealth.services.reddit.di

import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.IO_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.MAIN_IMMEDIATE_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.common.util.TimeValue
import com.cosmos.stealth.core.network.data.converter.MoshiContentConverter
import com.cosmos.stealth.core.network.util.UrlSubstitutor
import com.cosmos.stealth.core.network.data.converter.moshi
import com.cosmos.stealth.services.reddit.data.remote.api.RedditApi
import com.cosmos.stealth.services.reddit.data.remote.api.DataRedditApi
import com.cosmos.stealth.services.reddit.data.remote.api.ScrapRedditApi
import com.cosmos.stealth.services.reddit.data.adapter.EditedAdapter
import com.cosmos.stealth.services.reddit.data.adapter.MediaMetadataAdapter
import com.cosmos.stealth.services.reddit.data.adapter.NullToEmptyStringAdapter
import com.cosmos.stealth.services.reddit.data.adapter.RepliesAdapter
import com.cosmos.stealth.services.reddit.data.model.AboutChild
import com.cosmos.stealth.services.reddit.data.model.AboutUserChild
import com.cosmos.stealth.services.reddit.data.model.Child
import com.cosmos.stealth.services.reddit.data.model.ChildType
import com.cosmos.stealth.services.reddit.data.model.CommentChild
import com.cosmos.stealth.services.reddit.data.model.MoreChild
import com.cosmos.stealth.services.reddit.data.model.PostChild
import com.cosmos.stealth.services.reddit.di.NetworkModule.Qualifier.REDDIT_QUALIFIER
import com.cosmos.stealth.services.reddit.di.NetworkModule.Qualifier.REDDIT_SCRAP_QUALIFIER
import com.cosmos.stealth.services.reddit.data.remote.JsonInterceptor
import com.cosmos.stealth.services.reddit.data.remote.RawJsonInterceptor
import com.cosmos.stealth.services.reddit.data.remote.RedditCookieJar
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

object NetworkModule {

    @Suppress("MagicNumber")
    private val TIMEOUT = TimeValue<Long>(60, TimeUnit.SECONDS)

    object Qualifier {
        val REDDIT_QUALIFIER = named("reddit")
        val REDDIT_SCRAP_QUALIFIER = named("reddit_scrap")
    }

    @Suppress("MemberNameEqualsClassName")
    val networkModule = module {
        single(REDDIT_QUALIFIER) { provideRedditMoshi() }
        single(REDDIT_QUALIFIER) { provideRedditOkHttpClient() }
        single(REDDIT_QUALIFIER) {
            provideRedditMoshiContentConverter(
                get(REDDIT_QUALIFIER),
                get(IO_DISPATCHER_QUALIFIER)
            )
        }
        single(REDDIT_QUALIFIER) {
            provideRedditHttpClient(
                get(REDDIT_QUALIFIER),
                get(REDDIT_QUALIFIER)
            )
        }
        single(REDDIT_QUALIFIER) { provideDataRedditApi(get(REDDIT_QUALIFIER), get()) }

        single(REDDIT_SCRAP_QUALIFIER) { provideRedditScrapOkHttpClient() }
        single(REDDIT_SCRAP_QUALIFIER) { provideRedditScrapHttpClient(get(REDDIT_SCRAP_QUALIFIER)) }
        single(REDDIT_SCRAP_QUALIFIER) {
            provideScrapRedditApi(
                get(REDDIT_SCRAP_QUALIFIER),
                get(),
                get(MAIN_IMMEDIATE_DISPATCHER_QUALIFIER),
                get(IO_DISPATCHER_QUALIFIER)
            )
        }
    }

    private fun provideRedditMoshi(): Moshi {
        return Moshi.Builder()
            .add(
                PolymorphicJsonAdapterFactory.of(Child::class.java, "kind")
                    .withSubtype(CommentChild::class.java, ChildType.t1.name)
                    .withSubtype(AboutUserChild::class.java, ChildType.t2.name)
                    .withSubtype(PostChild::class.java, ChildType.t3.name)
                    .withSubtype(AboutChild::class.java, ChildType.t5.name)
                    .withSubtype(MoreChild::class.java, ChildType.more.name)
            )
            .add(MediaMetadataAdapter.Factory)
            .add(RepliesAdapter())
            .add(EditedAdapter())
            .add(NullToEmptyStringAdapter())
            .build()
    }

    private fun provideRedditMoshiContentConverter(
        moshi: Moshi,
        ioDispatcher: CoroutineDispatcher
    ): MoshiContentConverter {
        return MoshiContentConverter(moshi, ioDispatcher)
    }

    private fun provideRedditOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(RawJsonInterceptor())
            .addInterceptor(JsonInterceptor())
            .connectTimeout(TIMEOUT.value, TIMEOUT.unit)
            .readTimeout(TIMEOUT.value, TIMEOUT.unit)
            .writeTimeout(TIMEOUT.value, TIMEOUT.unit)
            .build()
    }

    private fun provideRedditScrapOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT.value, TIMEOUT.unit)
            .readTimeout(TIMEOUT.value, TIMEOUT.unit)
            .writeTimeout(TIMEOUT.value, TIMEOUT.unit)
            .cookieJar(RedditCookieJar())
            .build()
    }

    private fun provideRedditHttpClient(
        okHttpClient: OkHttpClient,
        moshiContentConverter: MoshiContentConverter
    ): HttpClient {
        return HttpClient(OkHttp) {
            defaultRequest {
                url(RedditApi.BASE_URL)
            }

            install(ContentNegotiation) {
                moshi(moshiContentConverter)
            }

            engine {
                preconfigured = okHttpClient
            }

            expectSuccess = true
        }
    }

    private fun provideRedditScrapHttpClient(okHttpClient: OkHttpClient): HttpClient {
        return HttpClient(OkHttp) {
            defaultRequest {
                url(RedditApi.BASE_URL_OLD)
            }

            engine {
                preconfigured = okHttpClient
            }

            expectSuccess = true
        }
    }

    private fun provideDataRedditApi(httpClient: HttpClient, urlSubstitutor: UrlSubstitutor): RedditApi {
        return DataRedditApi(httpClient, urlSubstitutor)
    }

    private fun provideScrapRedditApi(
        httpClient: HttpClient,
        urlSubstitutor: UrlSubstitutor,
        mainImmediateDispatcher: CoroutineDispatcher,
        ioDispatcher: CoroutineDispatcher
    ): RedditApi {
        return ScrapRedditApi(httpClient, urlSubstitutor, mainImmediateDispatcher, ioDispatcher)
    }
}
