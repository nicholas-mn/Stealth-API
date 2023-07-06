package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Flair data
 *
 * @param type 
 * @param text 
 * @param url 
 */
@JsonClass(generateAdapter = true)
data class FlairData (
    @Json(name = "type")
    val type: Type,

    @Json(name = "text")
    val text: String? = null,

    @Json(name = "url")
    val url: String? = null
) {

    /**
     * 
     *
     * Values: text,image
     */
    @JsonClass(generateAdapter = false)
    enum class Type(val value: String) {
        @Json(name = "text") text("text"),
        @Json(name = "image") image("image");
    }
}

