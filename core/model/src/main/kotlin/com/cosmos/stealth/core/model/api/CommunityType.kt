package com.cosmos.stealth.core.model.api


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Community type
 *
 * Values: community
 */

@JsonClass(generateAdapter = false)
enum class CommunityType(val value: String) {

    @Json(name = "community")
    community("community");

    override fun toString(): String = value

    companion object {
        /**
         * Converts the provided [data] to a [String] on success, null otherwise.
         */
        fun encode(data: Any?): String? = if (data is CommunityType) "$data" else null

        /**
         * Returns a valid [CommunityType] for [data], null otherwise.
         */
        fun decode(data: Any?): CommunityType? = data?.let {
            val normalizedData = "$it".lowercase()
            values().firstOrNull { value ->
                it == value || normalizedData == "$value".lowercase()
            }
        }
    }
}
