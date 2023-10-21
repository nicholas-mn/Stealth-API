package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Service description
 *
 * @param name 
 * @param instance Target instance
 */
@JsonClass(generateAdapter = true)
data class Service (
    @Json(name = "name")
    val name: ServiceName,

    /* Target instance */
    @Json(name = "instance")
    val instance: String? = null
)
