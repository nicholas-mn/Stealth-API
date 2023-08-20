package com.cosmos.stealth.core.model.api


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Time filter
 *
 * Values: hour,day,week,month,year,all
 */
@Suppress("EnumNaming")
@JsonClass(generateAdapter = false)
enum class Time(val value: String) {

    @Json(name = "hour")
    hour("hour"),

    @Json(name = "day")
    day("day"),

    @Json(name = "week")
    week("week"),

    @Json(name = "month")
    month("month"),

    @Json(name = "year")
    year("year"),

    @Json(name = "all")
    all("all");

    override fun toString(): String = value

    companion object {
        /**
         * Converts the provided [data] to a [String] on success, null otherwise.
         */
        fun encode(data: Any?): String? = if (data is Time) "$data" else null

        /**
         * Returns a valid [Time] for [data], null otherwise.
         */
        fun decode(data: Any?): Time? = data?.let {
            val normalizedData = "$it".lowercase()
            values().firstOrNull { value ->
                it == value || normalizedData == "$value".lowercase()
            }
        }
    }
}
