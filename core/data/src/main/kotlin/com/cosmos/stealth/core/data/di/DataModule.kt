package com.cosmos.stealth.core.data.di

import com.cosmos.stealth.core.common.di.DispatchersModule
import com.cosmos.stealth.core.common.util.TimeValue
import com.cosmos.stealth.core.data.di.DataModule.Qualifier.DASH_QUALIFIER
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.xml.DefaultXml
import io.ktor.serialization.kotlinx.xml.xml
import okhttp3.OkHttpClient
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.util.concurrent.TimeUnit

@Module(includes = [DispatchersModule::class])
@ComponentScan("com.cosmos.stealth.core.data")
class DataModule {

    @Suppress("MagicNumber", "VariableNaming")
    private val TIMEOUT = TimeValue<Long>(5, TimeUnit.SECONDS)

    object Qualifier {
        const val DASH_QUALIFIER = "dash"
    }

    @Single
    @Named(DASH_QUALIFIER)
    fun provideDashOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT.value, TIMEOUT.unit)
            .readTimeout(TIMEOUT.value, TIMEOUT.unit)
            .writeTimeout(TIMEOUT.value, TIMEOUT.unit)
            .build()
    }

    @Single
    @Named(DASH_QUALIFIER)
    fun provideDashHttpClient(@Named(DASH_QUALIFIER) okHttpClient: OkHttpClient): HttpClient {
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
}
