package com.cosmos.stealth.services.reddit.util.extension

import com.cosmos.stealth.core.model.api.PosterType

fun String?.toPosterType(): PosterType {
    return when (this) {
        "admin" -> PosterType.admin
        "moderator" -> PosterType.moderator
        else -> PosterType.regular
    }
}

fun String.getRefLink(instance: String): String {
    return "https://$instance$this"
}
