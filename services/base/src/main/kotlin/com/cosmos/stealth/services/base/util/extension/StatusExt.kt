package com.cosmos.stealth.services.base.util.extension

import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.Status
import com.cosmos.stealth.core.network.data.exception.BadResponseException
import com.cosmos.stealth.core.network.util.Resource
import java.net.HttpURLConnection

val Status.isSuccess: Boolean
    get() = this.code == HttpURLConnection.HTTP_OK

fun Status?.orInternalError(service: Service): Status {
    return this ?: Status(service, HttpURLConnection.HTTP_INTERNAL_ERROR)
}

fun <T> Status.toError(): Resource<T> {
    return when (this.code) {
        HttpURLConnection.HTTP_BAD_GATEWAY -> Resource.Exception(BadResponseException(this.error.orEmpty()))
        else -> Resource.Error(this.code, this.error.orEmpty())
    }
}
