package com.cosmos.stealth.core.model.data

import com.cosmos.stealth.core.model.api.FeedableType
import com.cosmos.stealth.core.model.api.SearchType
import com.cosmos.stealth.core.model.api.Sort

object Default {
    const val LIMIT = 25
    const val POST_LIMIT = 200

    val SORT = Sort.best

    val SEARCH_TYPE = SearchType.feedable

    val USER_FEEDABLE_TYPE = FeedableType.post
}
