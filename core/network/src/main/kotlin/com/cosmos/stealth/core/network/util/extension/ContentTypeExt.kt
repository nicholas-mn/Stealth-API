package com.cosmos.stealth.core.network.util.extension

import io.ktor.http.ContentType

val ContentType.mime: String
    get() = "$contentType/$contentSubtype"

val ContentType?.isImage: Boolean
    get() = this?.contentType == "image"

val ContentType?.isGif: Boolean
    get() = this?.contentSubtype == "gif"

val ContentType?.isVideo: Boolean
    get() = this?.contentType == "video"
