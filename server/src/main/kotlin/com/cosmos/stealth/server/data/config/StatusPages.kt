package com.cosmos.stealth.server.data.config

import com.cosmos.stealth.core.common.util.MessageHandler
import com.cosmos.stealth.core.model.api.Error
import com.cosmos.stealth.core.network.data.exception.BadResponseException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.ContentTransformationException
import io.ktor.server.response.respond
import java.util.Locale

fun Application.configureStatusPage() {
    install(StatusPages) {
        exception<Throwable> { call, throwable ->
            val statusCode: HttpStatusCode
            val errorMessage: String

            when (throwable) {
                is ContentTransformationException, is BadRequestException -> {
                    statusCode = HttpStatusCode.BadRequest
                    errorMessage = MessageHandler.getString(Locale.ENGLISH, "common.error.malformed_request")
                }
                is IllegalStateException, is IllegalArgumentException -> {
                    statusCode = HttpStatusCode.BadRequest
                    errorMessage = throwable.message.orEmpty()
                }
                is UnsupportedOperationException -> {
                    statusCode = HttpStatusCode.NotImplemented
                    errorMessage = MessageHandler.getString(Locale.ENGLISH, "common.error.not_supported")
                }
                is BadResponseException -> {
                    statusCode = HttpStatusCode.BadGateway
                    errorMessage = throwable.message.orEmpty()
                }
                else -> {
                    statusCode = HttpStatusCode.InternalServerError
                    errorMessage = MessageHandler.getString(Locale.ENGLISH, "common.error.generic")
                }
            }

            call.respond(statusCode, Error(statusCode.value, errorMessage))
        }
    }
}
