package com.cosmos.stealth.core.model.data

import com.cosmos.stealth.core.model.api.AfterKey
import com.cosmos.stealth.core.model.api.Feedable

data class SingleFeed(
    val items: List<Feedable>,

    val afterKey: AfterKey?
)
