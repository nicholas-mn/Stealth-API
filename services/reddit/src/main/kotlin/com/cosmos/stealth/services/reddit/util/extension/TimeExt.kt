package com.cosmos.stealth.services.reddit.util.extension

import com.cosmos.stealth.core.model.api.Time
import com.cosmos.stealth.services.reddit.data.model.TimeSorting

val Time.timeSorting: TimeSorting
    get() = when (this) {
        Time.hour -> TimeSorting.HOUR
        Time.day -> TimeSorting.DAY
        Time.week -> TimeSorting.WEEK
        Time.month -> TimeSorting.MONTH
        Time.year -> TimeSorting.YEAR
        Time.all -> TimeSorting.ALL
    }
