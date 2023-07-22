package com.cosmos.stealth.services.lemmy.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Search type
 *
 * Values: community,user,feedable
 */
@Suppress("EnumNaming")
@JsonClass(generateAdapter = false)
enum class SearchType(val value: String) {

    @Json(name = "All")
    All("All"),

    @Json(name = "Comments")
    Comments("Comments"),


    @Json(name = "Posts")
    Posts("Posts"),


    @Json(name = "Communities")
    Communities("Communities"),


    @Json(name = "Users")
    Users("Users"),


    @Json(name = "Url")
    Url("Url");


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

