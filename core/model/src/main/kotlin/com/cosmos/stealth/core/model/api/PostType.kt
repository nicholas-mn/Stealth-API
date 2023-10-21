package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Type of content
 *
 * Values: text,image,video,link
 */
@Suppress("EnumNaming")
@JsonClass(generateAdapter = false)
enum class PostType(val value: String) {

    @Json(name = "text")
    text("text"),

    @Json(name = "image")
    image("image"),

    @Json(name = "video")
    video("video"),

    @Json(name = "link")
    link("link");

    override fun toString(): String = value

    companion object {
        /**
         * Converts the provided [data] to a [String] on success, null otherwise.
         */
        fun encode(data: Any?): String? = if (data is PostType) "$data" else null

        /**
         * Returns a valid [PostType] for [data], null otherwise.
         */
        fun decode(data: Any?): PostType? = data?.let {
          val normalizedData = "$it".lowercase()
          values().firstOrNull { value ->
            it == value || normalizedData == "$value".lowercase()
          }
        }
    }
}

