package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Error description
 *
 * @param message 
 */
@JsonClass(generateAdapter = true)
data class Error (
    @Json(name = "message")
    val message: String
)
