package com.cosmos.stealth.services.lemmy.util.extension

import com.cosmos.stealth.core.model.api.Order
import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.core.model.api.Time
import com.cosmos.stealth.core.model.data.Filtering
import com.cosmos.stealth.services.lemmy.data.model.SortType

fun SortType.toFiltering(): Filtering {
    return when (this) {
        SortType.Active,
        SortType.Hot -> Filtering(Sort.trending)

        SortType.New -> Filtering(Sort.date, Order.desc)

        SortType.Old -> Filtering(Sort.date, Order.asc)

        SortType.TopHour -> Filtering(Sort.score, Order.desc, Time.hour)

        SortType.TopSixHour,
        SortType.TopTwelveHour,
        SortType.TopDay -> Filtering(Sort.score, Order.desc, Time.day)

        SortType.TopWeek -> Filtering(Sort.score, Order.desc, Time.week)

        SortType.TopMonth ->Filtering(Sort.score, Order.desc, Time.month)

        SortType.TopThreeMonths,
        SortType.TopSixMonths,
        SortType.TopNineMonths,
        SortType.TopYear -> Filtering(Sort.score, Order.desc, Time.year)

        SortType.TopAll -> Filtering(Sort.score, Order.desc, Time.all)

        SortType.MostComments,
        SortType.NewComments -> Filtering(Sort.comments, Order.desc)
    }
}
