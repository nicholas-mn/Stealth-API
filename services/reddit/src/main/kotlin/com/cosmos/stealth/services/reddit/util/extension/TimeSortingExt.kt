package com.cosmos.stealth.services.reddit.util.extension

import com.cosmos.stealth.core.model.api.Time
import com.cosmos.stealth.services.reddit.data.model.TimeSorting

fun TimeSorting?.toTime(): Time {
    return when (this) {
        TimeSorting.HOUR -> Time.hour
        TimeSorting.DAY -> Time.day
        TimeSorting.WEEK -> Time.week
        TimeSorting.MONTH -> Time.month
        TimeSorting.YEAR -> Time.year
        TimeSorting.ALL -> Time.all
        null -> Time.day
    }
}
