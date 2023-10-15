package com.cosmos.stealth.services.reddit.data.repository

import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.IO_DISPATCHER_QUALIFIER
import com.cosmos.stealth.services.reddit.data.model.Credentials
import com.cosmos.stealth.services.reddit.data.model.Token
import com.cosmos.stealth.services.reddit.data.remote.api.RedditApi
import com.cosmos.stealth.services.reddit.di.RedditModule.Qualifier.GENERIC_QUALIFIER
import com.cosmos.stealth.services.reddit.di.RedditModule.Qualifier.REDDIT_QUALIFIER
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.ktor.http.parameters
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okio.buffer
import okio.source
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import java.util.concurrent.TimeUnit

@Single
internal class CredentialsRepository(
    @Named(REDDIT_QUALIFIER) private val dataRedditApi: RedditApi,
    @Named(GENERIC_QUALIFIER) private val moshi: Moshi,
    @Named(IO_DISPATCHER_QUALIFIER) private val ioDispatcher: CoroutineDispatcher
) {

    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + ioDispatcher)

    private var credentials: List<Credentials> = emptyList()

    private var refreshJob: Job? = null

    val accessToken: String?
        get() = credentials.randomOrNull()?.token?.accessToken

    init { init() }

    private fun init() {
        val adapter = moshi.adapter<List<Credentials>>(
            Types.newParameterizedType(List::class.java, Credentials::class.java)
        )

        credentials = runBlocking(ioDispatcher) {
            javaClass.classLoader.getResourceAsStream(CREDENTIALS_FILE).use { inputStream ->
                inputStream?.source()?.buffer()?.use { buffer ->
                    adapter.fromJson(buffer)
                }
            } ?: emptyList()
        }

        if (credentials.isNotEmpty()) {
            initTokens()
            registerRefreshJob()
        }
    }

    private fun initTokens() {
        scope.launch {
            credentials.map { creds ->
                async {
                    runCatching {
                        initToken(creds)
                    }.onSuccess { token ->
                        creds.token = token
                    }
                }
            }.awaitAll()
        }
    }

    private suspend fun initToken(credentials: Credentials): Token {
        return dataRedditApi.getAccessToken(
            parameters {
                append("grant_type", "https://oauth.reddit.com/grants/installed_client")
                append("device_id", "DO_NOT_TRACK_THIS_DEVICE")
                append("duration", "permanent")
            },
            credentials.bearer
        )
    }

    private fun registerRefreshJob() {
        refreshJob?.cancel()
        refreshJob = scope.launch {
            while (isActive) {
                delay(REFRESH_DELAY)

                credentials.map { creds ->
                    async {
                        runCatching {
                            refreshToken(creds)
                        }.onSuccess { token ->
                            creds.token = token
                        }
                    }
                }.awaitAll()
            }
        }
    }

    private suspend fun refreshToken(credentials: Credentials): Token {
        return dataRedditApi.getAccessToken(
            parameters {
                append("grant_type", "refresh_token")
                append("refresh_token", credentials.token?.refreshToken.orEmpty())
            },
            credentials.bearer
        )
    }

    companion object {
        private const val CREDENTIALS_FILE = "credentials.json"

        // Tokens expire after 1 day (86400 seconds)
        private val REFRESH_DELAY = TimeUnit.HOURS.toMillis(23)
    }
}
