package com.cosmos.stealth.services.lemmy.util.extension

import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.services.lemmy.data.model.SortType

val Sort.sortType: SortType
    get() = when (this) {
        Sort.best -> SortType.Hot
        Sort.new -> SortType.New
        Sort.top -> SortType.TopAll
    }
