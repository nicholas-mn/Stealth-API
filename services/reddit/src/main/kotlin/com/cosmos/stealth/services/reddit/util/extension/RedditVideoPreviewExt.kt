package com.cosmos.stealth.services.reddit.util.extension

import com.cosmos.stealth.core.model.api.Media
import com.cosmos.stealth.core.model.api.MediaSource
import com.cosmos.stealth.core.network.util.extension.mime
import com.cosmos.stealth.core.network.util.extension.mimeType
import com.cosmos.stealth.services.reddit.data.model.RedditVideoPreview
import com.cosmos.stealth.services.reddit.util.getRedditSoundTrack
import io.ktor.http.ContentType

fun RedditVideoPreview.toMedia(includeAudio: Boolean = false): Media? {
    val mime = fallbackUrl.mimeType?.mime ?: return null

    val audio = includeAudio
        .takeIf { it }
        ?.run {
            val audioUrl = getRedditSoundTrack(fallbackUrl)
            val media = Media(ContentType.Audio.MP4.mime, MediaSource(audioUrl))
            listOf(media)
        }

    return Media(mime, MediaSource(fallbackUrl, width, height), null, null, audio)
}
