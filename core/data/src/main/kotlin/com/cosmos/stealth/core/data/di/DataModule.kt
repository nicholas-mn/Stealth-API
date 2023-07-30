package com.cosmos.stealth.core.data.di

import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.DEFAULT_DISPATCHER_QUALIFIER
import com.cosmos.stealth.core.common.util.TimeValue
import com.cosmos.stealth.core.data.di.DataModule.Qualifier.DASH_QUALIFIER
import com.cosmos.stealth.core.data.mapper.DashMapper
import com.cosmos.stealth.core.data.repository.DashRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.xml.DefaultXml
import io.ktor.serialization.kotlinx.xml.xml
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

object DataModule {

    @Suppress("MagicNumber")
    private val TIMEOUT = TimeValue<Long>(5, TimeUnit.SECONDS)

    object Qualifier {
        val DASH_QUALIFIER = named("dash")
    }

    @Suppress("MemberNameEqualsClassName")
    val dataModule = module {
        single(DASH_QUALIFIER) { provideDashOkHttpClient() }
        single(DASH_QUALIFIER) { provideDashHttpClient(get(DASH_QUALIFIER)) }
        single { provideDashMapper(get(DEFAULT_DISPATCHER_QUALIFIER)) }
        single { provideDashRepository(get(DASH_QUALIFIER), get()) }
    }

    private fun provideDashOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT.value, TIMEOUT.unit)
            .readTimeout(TIMEOUT.value, TIMEOUT.unit)
            .writeTimeout(TIMEOUT.value, TIMEOUT.unit)
            .build()
    }

    private fun provideDashHttpClient(okHttpClient: OkHttpClient): HttpClient {
        return HttpClient(OkHttp) {
            install(ContentNegotiation) {
                xml(
                    format = DefaultXml.copy { defaultPolicy { ignoreUnknownChildren() } },
                    contentType = ContentType("application", "dash+xml")
                )
            }

            engine {
                preconfigured = okHttpClient
            }

            expectSuccess = true
        }
    }

    private fun provideDashMapper(defaultDispatcher: CoroutineDispatcher): DashMapper {
        return DashMapper(defaultDispatcher)
    }

    private fun provideDashRepository(httpClient: HttpClient, dashMapper: DashMapper): DashRepository {
        return DashRepository(httpClient, dashMapper)
    }
}
