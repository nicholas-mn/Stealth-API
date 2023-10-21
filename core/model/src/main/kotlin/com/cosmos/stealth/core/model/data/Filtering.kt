package com.cosmos.stealth.core.model.data

import com.cosmos.stealth.core.model.api.Order
import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.core.model.api.Time

data class Filtering(
    val sort: Sort = Default.SORT,

    val order: Order = Default.ORDER,

    val time: Time = Default.TIME
)
