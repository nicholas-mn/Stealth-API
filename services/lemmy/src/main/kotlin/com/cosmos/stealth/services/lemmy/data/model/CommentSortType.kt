package com.cosmos.stealth.services.lemmy.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Comment sort type
 *
 * Values: Hot,Top,New,Old
 */
@Suppress("EnumNaming")
@JsonClass(generateAdapter = false)
enum class CommentSortType(val value: String) {

    @Json(name = "Hot")
    Hot("Hot"),

    @Json(name = "Top")
    Top("Top"),

    @Json(name = "New")
    New("New"),


    @Json(name = "Old")
    Old("Old");


    override fun toString(): String = value

    companion object {
        /**
         * Converts the provided [data] to a [String] on success, null otherwise.
         */
        fun encode(data: Any?): String? = if (data is CommentSortType) "$data" else null

        /**
         * Returns a valid [CommentSortType] for [data], null otherwise.
         */
        fun decode(data: Any?): CommentSortType? = data?.let {
          val normalizedData = "$it".lowercase()
          values().firstOrNull { value ->
            it == value || normalizedData == "$value".lowercase()
          }
        }
    }
}

