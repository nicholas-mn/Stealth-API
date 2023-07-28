package com.cosmos.stealth.services.base.util.extension

import com.cosmos.stealth.core.common.util.extension.interlace
import com.cosmos.stealth.core.model.api.Feedable
import com.cosmos.stealth.core.model.api.Postable
import com.cosmos.stealth.core.model.api.Sort

fun List<List<Feedable>>.sortPosts(sort: Sort): List<Feedable> {
    return when (sort) {
        // If sorting is set to NEW, simply flatten the lists and sort the posts by date
        Sort.new -> this.flatten().sortedByDescending { (it as Postable).created }
        // If sorting is set to TOP, simply flatten the lists and sort the posts by score
        Sort.top -> this.flatten().sortedByDescending { (it as Postable).upvotes }
        // For all the other sorting methods, interlace the lists to have a consistent result
        // [['a', 'b', 'c'], ['e', 'f', 'g'], ['h', 'i']] ==> ['a', 'e', 'h', 'b', 'f', 'i', 'c', 'g']
        else -> this.interlace()
    }
}
