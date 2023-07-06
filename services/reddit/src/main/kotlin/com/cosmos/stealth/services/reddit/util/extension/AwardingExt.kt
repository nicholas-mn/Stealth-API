package com.cosmos.stealth.services.reddit.util.extension

import com.cosmos.stealth.core.model.api.Award
import com.cosmos.stealth.core.model.api.MediaSource
import com.cosmos.stealth.services.reddit.data.model.Awarding

fun Awarding.toAward(): Award {
    return Award(
        count,
        MediaSource(url),
        null,
        null,
        resizedIcons.map { it.toMediaSource() }
    )
}
