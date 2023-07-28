package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Info to request additional, unloaded content
 *
 * @param service
 * @param id 
 * @param count 
 * @param content 
 * @param parentId 
 * @param depth 
 */
@JsonClass(generateAdapter = true)
data class Appendable (
    @Json(name = "service")
    val service: Service,

    @Json(name = "id")
    val id: String,

    @Json(name = "count")
    val count: Int,

    @Json(name = "content")
    val content: List<String>,

    @Json(name = "parentId")
    val parentId: String,

    @Json(name = "depth")
    val depth: Int
) : Feedable(FeedableType.more)
