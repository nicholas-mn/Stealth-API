package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Pagination key for a specific service
 *
 * @param service 
 * @param key 
 */
@JsonClass(generateAdapter = true)
data class After (
    @Json(name = "service")
    val service: Service,

    @Json(name = "key")
    val key: AfterKey
)
