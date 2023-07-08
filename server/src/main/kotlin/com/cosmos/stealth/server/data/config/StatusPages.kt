package com.cosmos.stealth.server.data.config

import com.cosmos.stealth.core.model.api.Error
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.ContentTransformationException
import io.ktor.server.response.respond

fun Application.configureStatusPage() {
    install(StatusPages) {
        exception<Throwable> { call, throwable ->
            val statusCode: HttpStatusCode
            val errorMessage: String

            when (throwable) {
                is ContentTransformationException -> {
                    statusCode = HttpStatusCode.BadRequest
                    errorMessage = "Malformed request"
                }
                is IllegalStateException -> {
                    statusCode = HttpStatusCode.BadRequest
                    errorMessage = throwable.message.orEmpty()
                }
                else -> {
                    statusCode = HttpStatusCode.InternalServerError
                    errorMessage = "Something went wrong"
                }
            }

            call.respond(statusCode, Error(statusCode.value, errorMessage))
        }
    }
}
