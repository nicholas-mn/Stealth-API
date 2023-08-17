package com.cosmos.stealth.services.teddit.di

import com.cosmos.stealth.core.common.di.DispatchersModule
import com.cosmos.stealth.core.common.util.TimeValue
import com.cosmos.stealth.core.network.data.converter.MoshiContentConverter
import com.cosmos.stealth.core.network.data.converter.moshi
import com.cosmos.stealth.core.network.di.NetworkModule
import com.cosmos.stealth.services.reddit.data.remote.RawJsonInterceptor
import com.cosmos.stealth.services.reddit.di.RedditModule.Qualifier.REDDIT_QUALIFIER
import com.cosmos.stealth.services.teddit.data.remote.TargetRedditInterceptor
import com.cosmos.stealth.services.teddit.di.TedditModule.Qualifier.TEDDIT_QUALIFIER
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import okhttp3.OkHttpClient
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.util.concurrent.TimeUnit

@Module(includes = [DispatchersModule::class, NetworkModule::class])
@ComponentScan("com.cosmos.stealth.services.teddit")
class TedditModule {

    @Suppress("MagicNumber", "VariableNaming")
    private val TIMEOUT = TimeValue<Long>(60, TimeUnit.SECONDS)

    object Qualifier {
        const val TEDDIT_QUALIFIER = "teddit"
    }

    @Single
    @Named(TEDDIT_QUALIFIER)
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(RawJsonInterceptor())
            .addInterceptor(TargetRedditInterceptor())
            .connectTimeout(TIMEOUT.value, TIMEOUT.unit)
            .readTimeout(TIMEOUT.value, TIMEOUT.unit)
            .writeTimeout(TIMEOUT.value, TIMEOUT.unit)
            .build()
    }

    @Single
    @Named(TEDDIT_QUALIFIER)
    fun provideHttpClient(
        @Named(TEDDIT_QUALIFIER) okHttpClient: OkHttpClient,
        @Named(REDDIT_QUALIFIER) moshiContentConverter: MoshiContentConverter
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
}
