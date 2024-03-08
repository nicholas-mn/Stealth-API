package com.cosmos.stealth.services.lemmy.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PersonView(
    @Json(name = "person")
    val person: Person,

    @Json(name = "counts")
    val counts: PersonAggregates,

    @Json(name = "is_admin")
    val isAdmin: Boolean?
)
