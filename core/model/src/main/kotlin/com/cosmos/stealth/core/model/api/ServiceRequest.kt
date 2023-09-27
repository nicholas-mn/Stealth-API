package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Request for a feed from a specific service
 *
 * @param service 
 * @param communities 
 */
@JsonClass(generateAdapter = true)
data class ServiceRequest (
    @Json(name = "service")
    val service: Service,

    @Json(name = "communities")
    val communities: List<String>?
)
