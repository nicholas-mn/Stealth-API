package com.cosmos.stealth.services.reddit.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlin.io.encoding.Base64

@JsonClass(generateAdapter = true)
internal data class Credentials(
    @Json(name = "clientId")
    val clientId: String,

    @Json(name = "clientSecret")
    val clientSecret: String? = null,

    @Json(name = "token")
    var token: Token? = null
) {
    val bearer: String
        get() = Base64.encode("$clientId:".toByteArray())
}
