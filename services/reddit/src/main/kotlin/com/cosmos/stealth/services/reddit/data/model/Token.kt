package com.cosmos.stealth.services.reddit.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Token(
    @Json(name = "access_token")
    val accessToken: String,

    @Json(name = "device_id")
    val deviceId: String,

    @Json(name = "expires_in")
    val expiresIn: Int,

    @Json(name = "refresh_token")
    val refreshToken: String?,

    @Json(name = "scope")
    val scope: String,

    @Json(name = "token_type")
    val tokenType: String
)
