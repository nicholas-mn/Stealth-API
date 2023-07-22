package com.cosmos.stealth.services.lemmy.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostView(
    @Json(name = "post")
    val post: Post,

    @Json(name = "creator")
    val creator: Person,

    @Json(name = "community")
    val community: Community,

    @Json(name = "counts")
    val counts: PostAggregates
)
