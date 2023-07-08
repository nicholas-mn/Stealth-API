package com.cosmos.stealth.server.util.extension

import com.cosmos.stealth.core.model.api.Error
import com.cosmos.stealth.core.model.data.Headers
import com.cosmos.stealth.core.model.data.RequestInfo
import com.cosmos.stealth.core.network.util.Resource
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.origin
import io.ktor.server.response.respond

val ApplicationCall.info: RequestInfo
    get() {
        val proxyMode = request.headers[Headers.X_PROXY_MODE].toBoolean()
        val remoteHost = request.origin.remoteHost

        return RequestInfo(remoteHost, proxyMode)
    }

fun ApplicationCall.getPath(name: String): String? {
    return parameters[name]
}

fun ApplicationCall.getQuery(name: String): String? {
    return request.queryParameters[name]
}

suspend inline fun <reified T : Any> ApplicationCall.respondWithResource(resource: Resource<T>) {
    when (resource) {
        is Resource.Success -> respond(HttpStatusCode.OK, resource.data)
        is Resource.Error -> respond(HttpStatusCode.BadRequest, Error(resource.code, resource.message))
        is Resource.Exception -> throw resource.throwable
    }
}
