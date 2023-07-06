package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * User type
 *
 * Values: user
 */

@JsonClass(generateAdapter = false)
enum class UserType(val value: String) {

    @Json(name = "user")
    user("user");

    override fun toString(): String = value

    companion object {
        /**
         * Converts the provided [data] to a [String] on success, null otherwise.
         */
        fun encode(data: Any?): String? = if (data is UserType) "$data" else null

        /**
         * Returns a valid [UserType] for [data], null otherwise.
         */
        fun decode(data: Any?): UserType? = data?.let {
            val normalizedData = "$it".lowercase()
            values().firstOrNull { value ->
                it == value || normalizedData == "$value".lowercase()
            }
        }
    }
}
