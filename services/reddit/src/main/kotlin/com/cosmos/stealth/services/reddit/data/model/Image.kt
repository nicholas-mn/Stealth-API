package com.cosmos.stealth.services.reddit.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Image (
    @Json(name = "source")
    val imageSource: ImageSource,

    @Json(name = "resolutions")
    val resolutions: List<ImageSource>,

    @Json(name = "variants")
    val variants: Variants?
)
