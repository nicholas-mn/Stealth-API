package com.cosmos.stealth.services.reddit.util

import com.cosmos.stealth.core.model.api.Flair
import com.cosmos.stealth.core.model.api.FlairData
import com.cosmos.stealth.services.reddit.data.model.RichText

fun toFlair(flairRichText: List<RichText>?, flair: String?, background: String? = null): Flair? {
    val flairDataList = mutableListOf<FlairData>()

    if (!flairRichText.isNullOrEmpty()) {
        flairRichText.forEach { richText ->
            when {
                !richText.t.isNullOrBlank() -> flairDataList.add(FlairData(FlairData.Type.text, richText.t))
                !richText.u.isNullOrEmpty() -> flairDataList.add(FlairData(FlairData.Type.image, richText.u))
            }
        }
    } else if (!flair.isNullOrEmpty()) {
        flairDataList.add(FlairData(FlairData.Type.text, flair))
    }

    return flairDataList
        .takeIf { it.isNotEmpty() }
        ?.run { Flair(toList(), background) }
}
