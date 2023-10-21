package com.cosmos.stealth.core.network.util

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

class LinkValidator(link: String?) {

    val validUrl: HttpUrl?

    val isValid: Boolean
        get() = validUrl != null

    init {
        validUrl = link?.run {
            val builder = StringBuilder()

            if (!link.startsWith("http:", ignoreCase = true) &&
                !link.startsWith("https:", ignoreCase = true)) {
                builder.append("https://")
            }

            builder.append(link)

            builder.toString().toHttpUrlOrNull()
        }
    }
}
