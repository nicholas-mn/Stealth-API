package com.cosmos.stealth.core.model.data

import com.cosmos.stealth.core.model.api.FeedableType
import com.cosmos.stealth.core.model.api.Order
import com.cosmos.stealth.core.model.api.SearchType
import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.core.model.api.Time

object Default {
    const val LIMIT = 25
    const val POST_LIMIT = 200

    val SORT = Sort.trending
    val ORDER = Order.desc
    val TIME = Time.day

    val SEARCH_TYPE = SearchType.feedable

    val USER_FEEDABLE_TYPE = FeedableType.post
}
