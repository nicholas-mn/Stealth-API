package com.cosmos.stealth.services.teddit.data.model

import com.cosmos.stealth.services.reddit.data.model.AboutUserChild
import com.cosmos.stealth.services.reddit.data.model.Listing
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TedditUser(
    @Json(name = "about")
    val about: AboutUserChild,

    @Json(name = "overview")
    val overview: Listing
)
