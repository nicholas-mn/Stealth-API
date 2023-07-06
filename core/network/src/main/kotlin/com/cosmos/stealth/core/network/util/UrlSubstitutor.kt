package com.cosmos.stealth.core.network.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.apache.commons.text.StringSubstitutor

class UrlSubstitutor(private val defaultDispatcher: CoroutineDispatcher) {

    suspend fun buildUrl(baseUrl: String, path: String, vararg params: Pair<String, String>): String =
        withContext(defaultDispatcher) {
            val baseHttpUrl = baseUrl.toHttpUrlOrNull() ?: error("$baseUrl is not a valid URL")
            buildUrl(baseHttpUrl, path, *params)
        }

    suspend fun buildUrl(baseUrl: HttpUrl, path: String, vararg params: Pair<String, String>): String =
        withContext(defaultDispatcher) {
            val encodedPath = buildUrl(path, *params)

            baseUrl.newBuilder()
                .encodedPath(encodedPath)
                .build()
                .toString()
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
