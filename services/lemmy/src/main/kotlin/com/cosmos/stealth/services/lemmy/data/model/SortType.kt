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
enum class SortType(val value: String) {

    @Json(name = "Active")
    Active("Active"),

    @Json(name = "Hot")
    Hot("Hot"),


    @Json(name = "New")
    New("New"),


    @Json(name = "Old")
    Old("Old"),


    @Json(name = "TopDay")
    TopDay("TopDay"),


    @Json(name = "TopWeek")
    TopWeek("TopWeek"),


    @Json(name = "TopMonth")
    TopMonth("TopMonth"),


    @Json(name = "TopYear")
    TopYear("TopYear"),


    @Json(name = "TopAll")
    TopAll("TopAll"),


    @Json(name = "MostComments")
    MostComments("MostComments"),


    @Json(name = "NewComments")
    NewComments("NewComments"),


    @Json(name = "TopHour")
    TopHour("TopHour"),


    @Json(name = "TopSixHour")
    TopSixHour("TopSixHour"),


    @Json(name = "TopTwelveHour")
    TopTwelveHour("TopTwelveHour"),


    @Json(name = "TopThreeMonths")
    TopThreeMonths("TopThreeMonths"),


    @Json(name = "TopSixMonths")
    TopSixMonths("TopSixMonths"),


    @Json(name = "TopNineMonths")
    TopNineMonths("TopNineMonths");


    override fun toString(): String = value

    companion object {
        /**
         * Converts the provided [data] to a [String] on success, null otherwise.
         */
        fun encode(data: Any?): String? = if (data is SortType) "$data" else null

        /**
         * Returns a valid [SortType] for [data], null otherwise.
         */
        fun decode(data: Any?): SortType? = data?.let {
          val normalizedData = "$it".lowercase()
          values().firstOrNull { value ->
            it == value || normalizedData == "$value".lowercase()
          }
        }
    }
}

