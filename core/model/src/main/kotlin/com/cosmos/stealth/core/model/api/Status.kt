package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Status for a service request
 *
 * @param service 
 * @param code 
 * @param message
 */
@JsonClass(generateAdapter = true)
data class Status (
    @Json(name = "service")
    val service: Service,

    @Json(name = "code")
    val code: Int,

    @Json(name = "message")
    val message: String? = null
)
