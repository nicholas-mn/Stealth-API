package com.cosmos.stealth.services.reddit.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RichText(
    @Json(name = "t")
    val t: String?,

    @Json(name = "u")
    val u: String?
)
