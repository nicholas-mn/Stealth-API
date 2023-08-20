package com.cosmos.stealth.services.reddit.util.extension

import com.cosmos.stealth.core.model.api.Order
import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.core.model.data.Filtering
import com.cosmos.stealth.services.reddit.data.model.Sorting
import com.cosmos.stealth.services.reddit.data.model.Sort as RedditSort

fun Filtering.toRedditSort(isPost: Boolean = false): Sorting = when (sort) {
    Sort.trending -> if (isPost) Sorting(RedditSort.BEST) else Sorting(RedditSort.HOT)
    Sort.date -> if (order == Order.desc) Sorting(RedditSort.NEW) else Sorting(RedditSort.OLD)
    Sort.score -> {
        if (order == Order.desc) {
            Sorting(RedditSort.TOP, time.timeSorting)
        } else {
            Sorting(RedditSort.CONTROVERSIAL, time.timeSorting)
        }
    }

    Sort.comments -> Sorting(RedditSort.COMMENTS, time.timeSorting)
    Sort.relevance -> Sorting(RedditSort.RELEVANCE, time.timeSorting)
}
