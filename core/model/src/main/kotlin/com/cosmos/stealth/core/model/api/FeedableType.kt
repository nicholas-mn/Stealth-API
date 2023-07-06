package com.cosmos.stealth.core.model.api


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Feedable type
 *
 * Values: post,comment
 */

@JsonClass(generateAdapter = false)
enum class FeedableType(val value: String) {

    @Json(name = "post")
    post("post"),

    @Json(name = "comment")
    comment("comment"),

    @Json(name = "more")
    more("more");

    override fun toString(): String = value

    companion object {
        /**
         * Converts the provided [data] to a [String] on success, null otherwise.
         */
        fun encode(data: Any?): String? = if (data is FeedableType) "$data" else null

        /**
         * Returns a valid [FeedableType] for [data], null otherwise.
         */
        fun decode(data: Any?): FeedableType? = data?.let {
          val normalizedData = "$it".lowercase()
          values().firstOrNull { value ->
            it == value || normalizedData == "$value".lowercase()
          }
        }
    }
}

