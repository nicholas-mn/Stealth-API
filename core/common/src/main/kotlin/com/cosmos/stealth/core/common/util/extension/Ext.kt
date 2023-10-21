package com.cosmos.stealth.core.common.util.extension

import io.ktor.util.logging.Logger
import org.slf4j.LoggerFactory

fun <T> T.asList(): List<T> {
    return listOf(this)
}

inline fun <reified T> T.getLogger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}
