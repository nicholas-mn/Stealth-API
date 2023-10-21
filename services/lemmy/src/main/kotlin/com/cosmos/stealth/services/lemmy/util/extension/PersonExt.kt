package com.cosmos.stealth.services.lemmy.util.extension

import com.cosmos.stealth.core.model.api.PosterType
import com.cosmos.stealth.services.lemmy.data.model.Person
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

fun Person.toPosterType(): PosterType {
    return when {
        admin -> PosterType.admin
        botAccount -> PosterType.bot
        else -> PosterType.regular
    }
}

fun Person.getAuthorName(instanceDomain: String?): String {
    val domain = actorId.toHttpUrlOrNull()?.host
    return if (domain != null && domain != instanceDomain) "$name@$domain" else name
}
