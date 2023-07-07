package com.cosmos.stealth.core.model.api


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Type of poster
 *
 * Values: regular,moderator,admin
 */
@Suppress("EnumNaming")
@JsonClass(generateAdapter = false)
enum class PosterType(val value: String) {

    @Json(name = "regular")
    regular("regular"),

    @Json(name = "moderator")
    moderator("moderator"),

    @Json(name = "admin")
    admin("admin");

    override fun toString(): String = value

    companion object {
        /**
         * Converts the provided [data] to a [String] on success, null otherwise.
         */
        fun encode(data: Any?): String? = if (data is PosterType) "$data" else null

        /**
         * Returns a valid [PosterType] for [data], null otherwise.
         */
        fun decode(data: Any?): PosterType? = data?.let {
          val normalizedData = "$it".lowercase()
          values().firstOrNull { value ->
            it == value || normalizedData == "$value".lowercase()
          }
        }
    }
}

