package com.cosmos.stealth.core.network.util

import com.cosmos.stealth.core.common.di.DispatchersModule.Qualifier.DEFAULT_DISPATCHER_QUALIFIER
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import org.apache.commons.text.StringSubstitutor
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single
class UrlSubstitutor(@Named(DEFAULT_DISPATCHER_QUALIFIER) private val defaultDispatcher: CoroutineDispatcher) {

    suspend fun buildUrl(baseUrl: String, path: String, vararg params: Pair<String, String>): String =
        withContext(defaultDispatcher) {
            val baseHttpUrl = LinkValidator(baseUrl).validUrl ?: error("$baseUrl is not a valid URL")
            buildUrl(baseHttpUrl, path, *params)
        }

    suspend fun buildUrl(baseUrl: HttpUrl, path: String, vararg params: Pair<String, String>): String =
        withContext(defaultDispatcher) {
            val encodedPath = buildUrl(path, *params)

            // Build the URL manually to avoid query parameter encoding
            // HttpUrl adds a trailing slash at the end of the URL, so it needs to be removed before appending our path
            baseUrl.toString().dropLast(1).plus(encodedPath)
        }

    suspend fun buildUrl(path: String, vararg params: Pair<String, String>): String =
        withContext(defaultDispatcher) {
            StringSubstitutor(mapOf(*params), PREFIX, SUFFIX)
                .apply { isEnableUndefinedVariableException = false }
                .replace(path)
        }

    companion object {
        private const val PREFIX = "{"
        private const val SUFFIX = "}"
    }
}
