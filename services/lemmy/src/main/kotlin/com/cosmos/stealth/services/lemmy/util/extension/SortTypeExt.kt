package com.cosmos.stealth.services.lemmy.util.extension

import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.services.lemmy.data.model.SortType

fun SortType.toSort(): Sort {
    return when (this) {
        SortType.New -> Sort.new
        SortType.Hot -> Sort.best
        SortType.TopDay,
        SortType.TopWeek,
        SortType.TopMonth,
        SortType.TopYear,
        SortType.TopAll,
        SortType.TopHour,
        SortType.TopSixHour,
        SortType.TopTwelveHour,
        SortType.TopThreeMonths,
        SortType.TopSixMonths,
        SortType.TopNineMonths -> Sort.top
        else -> Sort.best
    }
}
