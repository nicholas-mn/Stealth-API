package com.cosmos.stealth.services.reddit.util

import com.cosmos.stealth.core.model.api.Badge
import com.cosmos.stealth.core.model.api.BadgeData
import com.cosmos.stealth.services.reddit.data.model.RichText

fun toBadge(flairRichText: List<RichText>?, flair: String?, background: String? = null): Badge? {
    val badgeDataList = mutableListOf<BadgeData>()

    if (!flairRichText.isNullOrEmpty()) {
        flairRichText.forEach { richText ->
            when {
                !richText.t.isNullOrBlank() -> badgeDataList.add(BadgeData(BadgeData.Type.text, richText.t))
                !richText.u.isNullOrEmpty() -> badgeDataList.add(BadgeData(BadgeData.Type.image, richText.u))
            }
        }
    } else if (!flair.isNullOrEmpty()) {
        badgeDataList.add(BadgeData(BadgeData.Type.text, flair))
    }

    return badgeDataList
        .takeIf { it.isNotEmpty() }
        ?.run { Badge(toList(), background) }
}
