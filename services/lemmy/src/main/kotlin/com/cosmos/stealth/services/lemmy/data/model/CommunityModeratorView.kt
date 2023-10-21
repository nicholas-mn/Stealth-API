package com.cosmos.stealth.services.lemmy.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommunityModeratorView(
    @Json(name = "community")
    val community: Community,

    @Json(name = "moderator")
    val moderator: Person
)
