package com.cosmos.stealth.services.base.util.extension

import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.Status
import com.cosmos.stealth.core.network.util.Resource
import java.net.HttpURLConnection

fun Resource<*>.toStatus(service: Service): Status {
    return when (this) {
        is Resource.Success -> Status(service, HttpURLConnection.HTTP_OK)
        is Resource.Error -> Status(service, code, message)
        is Resource.Exception -> Status(service, HttpURLConnection.HTTP_INTERNAL_ERROR, throwable.message)
    }
}
