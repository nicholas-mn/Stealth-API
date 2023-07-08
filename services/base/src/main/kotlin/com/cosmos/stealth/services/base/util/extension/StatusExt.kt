package com.cosmos.stealth.services.base.util.extension

import com.cosmos.stealth.core.model.api.Service
import com.cosmos.stealth.core.model.api.Status
import java.net.HttpURLConnection

val Status.isSuccess: Boolean
    get() = this.code == HttpURLConnection.HTTP_OK

val Status.isFailure: Boolean
    get() = !isSuccess

fun Status?.orInternalError(service: Service): Status {
    return this ?: Status(service, HttpURLConnection.HTTP_INTERNAL_ERROR)
}
