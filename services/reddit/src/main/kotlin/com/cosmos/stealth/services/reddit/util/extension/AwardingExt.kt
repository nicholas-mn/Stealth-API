package com.cosmos.stealth.services.reddit.util.extension

import com.cosmos.stealth.core.model.api.MediaSource
import com.cosmos.stealth.core.model.api.Reaction
import com.cosmos.stealth.services.reddit.data.model.Awarding

fun Awarding.toReaction(): Reaction {
    return Reaction(
        count,
        MediaSource(url),
        null,
        null,
        resizedIcons.map { it.toMediaSource() }
    )
}
