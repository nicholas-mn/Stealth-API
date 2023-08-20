package com.cosmos.stealth.services.base.util.extension

import com.cosmos.stealth.core.common.util.extension.interlace
import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.Order
import com.cosmos.stealth.core.model.api.Postable
import com.cosmos.stealth.core.model.api.Sort
import com.cosmos.stealth.core.model.data.Filtering

fun List<List<Feedable>>.sortPosts(filtering: Filtering): List<Feedable> {
    return when (filtering.sort) {
        // If sorting is set to date, simply flatten the lists and sort the posts by date
        Sort.date -> {
            if (filtering.order == Order.desc) {
                this.flatten().sortedByDescending { (it as Postable).created }
            } else {
                this.flatten().sortedBy { (it as Postable).created }
            }
        }
        // If sorting is set to score, simply flatten the lists and sort the posts by score
        Sort.score -> {
            if (filtering.order == Order.desc) {
                this.flatten().sortedByDescending { (it as Postable).upvotes }
            } else {
                this.flatten().sortedBy { (it as Postable).upvotes }
            }
        }
        // For all the other sorting methods, interlace the lists to have a consistent result
        // [['a', 'b', 'c'], ['e', 'f', 'g'], ['h', 'i']] ==> ['a', 'e', 'h', 'b', 'f', 'i', 'c', 'g']
        else -> this.interlace()
    }
}
