package com.cosmos.stealth.services.reddit.util.extension

import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.services.reddit.data.model.Sorting
import com.cosmos.stealth.services.reddit.data.model.TimeSorting

typealias RedditSort = com.cosmos.stealth.services.reddit.data.model.Sort

val Sort.redditSort: Sorting
    get() = when (this) {
        Sort.best -> Sorting(RedditSort.BEST)
        Sort.new -> Sorting(RedditSort.NEW)
        Sort.top -> Sorting(RedditSort.TOP, TimeSorting.ALL)
    }
