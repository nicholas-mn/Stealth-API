package com.cosmos.stealth.core.model.api


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Sort
 *
 * Values: best,new,top
 */

@JsonClass(generateAdapter = false)
enum class Sort(val value: String) {

    @Json(name = "best")
    best("best"),

    @Json(name = "new")
    new("new"),

    @Json(name = "top")
    top("top");

    override fun toString(): String = value

    companion object {
        /**
         * Converts the provided [data] to a [String] on success, null otherwise.
         */
        fun encode(data: Any?): String? = if (data is Sort) "$data" else null

        /**
         * Returns a valid [Sort] for [data], null otherwise.
         */
        fun decode(data: Any?): Sort? = data?.let {
          val normalizedData = "$it".lowercase()
          values().firstOrNull { value ->
            it == value || normalizedData == "$value".lowercase()
          }
        }
    }
}

