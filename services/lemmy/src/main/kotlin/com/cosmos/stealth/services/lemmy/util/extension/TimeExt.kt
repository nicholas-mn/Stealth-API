package com.cosmos.stealth.services.lemmy.util.extension

import com.cosmos.stealth.core.model.api.Time
import com.cosmos.stealth.services.lemmy.data.model.SortType

val Time.sortType: SortType
    get() = when (this) {
        Time.hour -> SortType.TopHour
        Time.day -> SortType.TopDay
        Time.week -> SortType.TopWeek
        Time.month -> SortType.TopMonth
        Time.year -> SortType.TopYear
        Time.all -> SortType.TopAll
    }
