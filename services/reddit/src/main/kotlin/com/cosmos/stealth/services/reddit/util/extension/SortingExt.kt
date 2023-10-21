package com.cosmos.stealth.services.reddit.util.extension

import com.cosmos.stealth.core.model.api.Order
import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.core.model.data.Filtering
import com.cosmos.stealth.services.reddit.data.model.Sorting
import com.cosmos.stealth.services.reddit.data.model.Sort as RedditSort

fun Sorting.toFiltering(): Filtering {
    return when (this.generalSorting) {
        RedditSort.QA,
        RedditSort.BEST,
        RedditSort.HOT -> Filtering(Sort.trending)

        RedditSort.RISING,
        RedditSort.NEW -> Filtering(Sort.date, Order.desc)

        RedditSort.TOP -> Filtering(Sort.score, Order.desc, timeSorting.toTime())

        RedditSort.CONTROVERSIAL -> Filtering(Sort.score, Order.asc, timeSorting.toTime())

        RedditSort.RELEVANCE -> Filtering(Sort.relevance, Order.desc, timeSorting.toTime())

        RedditSort.COMMENTS -> Filtering(Sort.comments, Order.desc, timeSorting.toTime())

        RedditSort.OLD -> Filtering(Sort.date, Order.asc)
    }
}
