package com.cosmos.stealth.services.reddit.util.extension

import com.cosmos.stealth.core.model.api.Media
import com.cosmos.stealth.core.model.api.MediaSource
import com.cosmos.stealth.core.model.api.Reaction
import com.cosmos.stealth.core.network.util.extension.mime
import com.cosmos.stealth.core.network.util.extension.mimeType
import com.cosmos.stealth.services.reddit.data.model.Awarding
import io.ktor.http.ContentType

fun Awarding.toReaction(): Reaction {
    val mime = url.mimeType ?: ContentType.Image.PNG

    val media = Media(
        mime.mime,
        MediaSource(url),
        null,
        resizedIcons.map { it.toMediaSource() }
    )

    return Reaction(
        count,
        media,
        null,
        null
    )
}
