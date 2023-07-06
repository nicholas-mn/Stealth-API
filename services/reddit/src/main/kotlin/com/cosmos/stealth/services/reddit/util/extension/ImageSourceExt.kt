package com.cosmos.stealth.services.reddit.util.extension

import com.cosmos.stealth.core.model.api.MediaSource
import com.cosmos.stealth.services.reddit.data.model.ImageSource

fun ImageSource.toMediaSource(): MediaSource {
    return MediaSource(url, width, height)
}
