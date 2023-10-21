package com.cosmos.stealth.services.lemmy.util.extension

import com.cosmos.stealth.core.common.util.getDateFromString

private const val DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

fun String?.toDateInMillis(): Long? {
    return this?.run { getDateFromString(DATETIME_FORMAT, this)?.time }
}
