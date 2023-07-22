package com.cosmos.stealth.services.lemmy.di

import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.DEFAULT_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.IO_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.common.util.TimeValue
import com.cosmos.stealth.core.network.data.converter.MoshiContentConverter
import com.cosmos.stealth.core.network.data.converter.moshi
import com.cosmos.stealth.core.network.util.UrlSubstitutor
import com.cosmos.stealth.services.lemmy.LemmyGateway
import com.cosmos.stealth.services.lemmy.data.mapper.CommentMapper
import com.cosmos.stealth.services.lemmy.data.mapper.CommunityMapper
import com.cosmos.stealth.services.lemmy.data.mapper.PostMapper
import com.cosmos.stealth.services.lemmy.data.mapper.UserMapper
import com.cosmos.stealth.services.lemmy.data.remote.api.HttpLemmyApi
import com.cosmos.stealth.services.lemmy.data.remote.api.LemmyApi
import com.cosmos.stealth.services.lemmy.data.repository.LemmyRepository
import com.cosmos.stealth.services.lemmy.di.LemmyNetworkModule.Qualifier.LEMMY_QUALIFIER
import com.squareup.moshi.Moshi
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

object LemmyNetworkModule {

    @Suppress("MagicNumber")
    private val TIMEOUT = TimeValue<Long>(60, TimeUnit.SECONDS)

    object Qualifier {
        val LEMMY_QUALIFIER = named("lemmy")
    }

    @Suppress("MemberNameEqualsClassName")
    val lemmyNetworkModule = module {
        single(LEMMY_QUALIFIER) { provideMoshi() }
        single(LEMMY_QUALIFIER) { provideMoshiConverter(get(LEMMY_QUALIFIER), get(IO_DISPATCHER_QUALIFIER)) }
        single(LEMMY_QUALIFIER) { provideOkHttpClient() }
        single(LEMMY_QUALIFIER) { provideHttpClient(get(LEMMY_QUALIFIER), get(LEMMY_QUALIFIER)) }
        single(LEMMY_QUALIFIER) { provideLemmyApi(get(LEMMY_QUALIFIER), get()) }
        single {
            provideLemmyRepository(
                get(LEMMY_QUALIFIER),
                get(),
                get(),
                get(),
                get(),
                get(DEFAULT_DISPATCHER_QUALIFIER)
            )
        }
        single { provideLemmyGateway(get()) }
    }

    private fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .build()
    }

    private fun provideMoshiConverter(moshi: Moshi, ioDispatcher: CoroutineDispatcher): MoshiContentConverter {
        return MoshiContentConverter(moshi, ioDispatcher)
    }

    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
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

    private fun provideLemmyApi(httpClient: HttpClient, urlSubstitutor: UrlSubstitutor): LemmyApi {
        return HttpLemmyApi(httpClient, urlSubstitutor)
    }

    @Suppress("LongParameterList")
    private fun provideLemmyRepository(
        lemmyApi: LemmyApi,
        postMapper: PostMapper,
        communityMapper: CommunityMapper,
        userMapper: UserMapper,
        commentMapper: CommentMapper,
        defaultDispatcher: CoroutineDispatcher
    ): LemmyRepository {
        return LemmyRepository(lemmyApi, postMapper, communityMapper, userMapper, commentMapper, defaultDispatcher)
    }

    private fun provideLemmyGateway(repository: LemmyRepository): LemmyGateway {
        return LemmyGateway(repository)
    }
}
