package com.cosmos.stealth.services.lemmy.util.extension

import com.cosmos.stealth.services.lemmy.data.model.Community
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

fun Community.getCommunityName(instanceDomain: String?): String {
    val domain = actorId.toHttpUrlOrNull()?.host
    return if (domain != null && domain != instanceDomain) "$name@$domain" else name
}
