package com.cosmos.stealth.core.model.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Sort order
 *
 * Values: asc,desc
 */
@Suppress("EnumNaming")
@JsonClass(generateAdapter = false)
enum class Order(val value: String) {

    @Json(name = "asc")
    asc("asc"),

    @Json(name = "desc")
    desc("desc");

    override fun toString(): String = value

    companion object {
        /**
         * Converts the provided [data] to a [String] on success, null otherwise.
         */
        fun encode(data: Any?): String? = if (data is Order) "$data" else null

        /**
         * Returns a valid [Order] for [data], null otherwise.
         */
        fun decode(data: Any?): Order? = data?.let {
            val normalizedData = "$it".lowercase()
            values().firstOrNull { value ->
                it == value || normalizedData == "$value".lowercase()
            }
        }
    }
}
