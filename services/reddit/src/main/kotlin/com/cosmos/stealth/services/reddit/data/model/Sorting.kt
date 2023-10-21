package com.cosmos.stealth.services.reddit.data.model

data class Sorting(
    val generalSorting: Sort,

    val timeSorting: TimeSorting? = null
)
