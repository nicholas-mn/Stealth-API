package com.cosmos.stealth.core.network.data.repository

import com.cosmos.stealth.core.network.util.Resource
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException

abstract class NetworkRepository {

    @Suppress("TooGenericExceptionCaught", "UNCHECKED_CAST")
    suspend fun <T, R> safeApiCall(
        apiCall: suspend () -> T,
        map: suspend (T) -> R = { it as R }
    ): Resource<R> {
        return try {
            val callResult = apiCall.invoke()
            val mappedResult = map.invoke(callResult)
            Resource.Success(mappedResult)
        } catch (e: Throwable) {
            parseError(e)
        }
    }

    @Suppress("TooGenericExceptionCaught")
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): Resource<T> {
        return try {
            val callResult = apiCall.invoke()
            Resource.Success(callResult)
        } catch (e: Throwable) {
            parseError(e)
        }
    }

    private fun <T> parseError(e: Throwable): Resource<T> {
        return when (e) {
            is RedirectResponseException -> Resource.Error(e.response.status.value, e.message)
            is ClientRequestException -> Resource.Error(e.response.status.value, e.message)
            is ServerResponseException -> Resource.Error(e.response.status.value, e.message)
            else -> Resource.Exception(e)
        }
    }
}
