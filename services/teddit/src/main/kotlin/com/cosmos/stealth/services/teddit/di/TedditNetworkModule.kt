package com.cosmos.stealth.services.teddit.di

import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.DEFAULT_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.common.util.TimeValue
import com.cosmos.stealth.core.network.data.converter.MoshiContentConverter
import com.cosmos.stealth.core.network.data.converter.moshi
import com.cosmos.stealth.core.network.util.UrlSubstitutor
import com.cosmos.stealth.services.reddit.data.mapper.CommentMapper
import com.cosmos.stealth.services.reddit.data.mapper.CommunityMapper
import com.cosmos.stealth.services.reddit.data.mapper.PostMapper
import com.cosmos.stealth.services.reddit.data.mapper.UserMapper
import com.cosmos.stealth.services.reddit.data.remote.RawJsonInterceptor
import com.cosmos.stealth.services.reddit.di.RedditNetworkModule.Qualifier.REDDIT_QUALIFIER
import com.cosmos.stealth.services.teddit.TedditGateway
import com.cosmos.stealth.services.teddit.data.remote.TargetRedditInterceptor
import com.cosmos.stealth.services.teddit.data.remote.api.DataTedditApi
import com.cosmos.stealth.services.teddit.data.remote.api.TedditApi
import com.cosmos.stealth.services.teddit.data.repository.TedditRepository
import com.cosmos.stealth.services.teddit.di.TedditNetworkModule.Qualifier.TEDDIT_QUALIFIER
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

object TedditNetworkModule {

    @Suppress("MagicNumber")
    private val TIMEOUT = TimeValue<Long>(60, TimeUnit.SECONDS)

    object Qualifier {
        val TEDDIT_QUALIFIER = named("teddit")
    }

    @Suppress("MemberNameEqualsClassName")
    val tedditNetworkModule = module {
        single(TEDDIT_QUALIFIER) { provideOkHttpClient() }
        single(TEDDIT_QUALIFIER) { provideHttpClient(get(TEDDIT_QUALIFIER), get(REDDIT_QUALIFIER)) }
        single(TEDDIT_QUALIFIER) { provideDataTedditApi(get(TEDDIT_QUALIFIER), get()) }

        single {
            provideTedditRepository(
                get(TEDDIT_QUALIFIER),
                get(),
                get(),
                get(),
                get(),
                get(DEFAULT_DISPATCHER_QUALIFIER)
            )
        }

        single { provideTedditGateway(get()) }
    }

    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(RawJsonInterceptor())
            .addInterceptor(TargetRedditInterceptor())
            .connectTimeout(TIMEOUT.value, TIMEOUT.unit)
            .readTimeout(TIMEOUT.value, TIMEOUT.unit)
            .writeTimeout(TIMEOUT.value, TIMEOUT.unit)
            .build()
    }

    private fun provideHttpClient(
        okHttpClient: OkHttpClient,
        moshiContentConverter: MoshiContentConverter
    ): HttpClient {
        return HttpClient(OkHttp) {
            install(ContentNegotiation) {
                moshi(moshiContentConverter)
            }

            engine {
                preconfigured = okHttpClient
            }

            expectSuccess = true
        }
    }

    private fun provideDataTedditApi(httpClient: HttpClient, urlSubstitutor: UrlSubstitutor): TedditApi {
        return DataTedditApi(httpClient, urlSubstitutor)
    }

    @Suppress("LongParameterList")
    private fun provideTedditRepository(
        dataTedditApi: TedditApi,
        postMapper: PostMapper,
        communityMapper: CommunityMapper,
        userMapper: UserMapper,
        commentMapper: CommentMapper,
        defaultDispatcher: CoroutineDispatcher
    ): TedditRepository {
        return TedditRepository(
            dataTedditApi,
            postMapper,
            communityMapper,
            userMapper,
            commentMapper,
            defaultDispatcher
        )
    }

    private fun provideTedditGateway(repository: TedditRepository): TedditGateway {
        return TedditGateway(repository)
    }
}
