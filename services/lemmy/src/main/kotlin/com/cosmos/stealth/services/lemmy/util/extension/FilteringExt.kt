package com.cosmos.stealth.services.lemmy.util.extension

import com.cosmos.stealth.core.model.api.Order
import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.core.model.data.Filtering
import com.cosmos.stealth.services.lemmy.data.model.CommentSortType
import com.cosmos.stealth.services.lemmy.data.model.SortType

val Filtering.sortType: SortType
    get() = when (sort) {
        Sort.trending -> SortType.Hot
        Sort.date -> if (order == Order.desc) SortType.New else SortType.Old
        Sort.score -> time.sortType
        Sort.comments -> SortType.MostComments
        Sort.relevance -> SortType.Hot
    }

val Filtering.commentSortType: CommentSortType
    get() = when (sort) {
        Sort.trending -> CommentSortType.Hot
        Sort.date -> if (order == Order.desc) CommentSortType.New else CommentSortType.Old
        Sort.score -> CommentSortType.Top
        Sort.comments,
        Sort.relevance -> CommentSortType.Hot
    }
