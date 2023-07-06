package com.cosmos.stealth.core.model.api


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Search type
 *
 * Values: community,user,feedable
 */

@JsonClass(generateAdapter = false)
enum class SearchType(val value: String) {

    @Json(name = "community")
    community("community"),

    @Json(name = "user")
    user("user"),

    @Json(name = "feedable")
    feedable("feedable");

    override fun toString(): String = value

    companion object {
        /**
         * Converts the provided [data] to a [String] on success, null otherwise.
         */
        fun encode(data: Any?): String? = if (data is SearchType) "$data" else null

        /**
         * Returns a valid [SearchType] for [data], null otherwise.
         */
        fun decode(data: Any?): SearchType? = data?.let {
          val normalizedData = "$it".lowercase()
          values().firstOrNull { value ->
            it == value || normalizedData == "$value".lowercase()
          }
        }
    }
}

