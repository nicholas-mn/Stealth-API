package com.cosmos.stealth.core.common.util.extension

import java.util.concurrent.TimeUnit

fun Long.toSeconds(): Long {
    return TimeUnit.MILLISECONDS.toSeconds(this)
}

fun Long.toMillis(): Long {
    return TimeUnit.SECONDS.toMillis(this)
}
