package com.cosmos.stealth.core.common.util.extension

fun <T> Collection<Collection<T>>.interlace(): List<T> {
    if (isEmpty()) return emptyList()

    val result = ArrayList<T>()

    val max = this.maxOf { it.size }

    for (i in 0..max) {
        this
            .mapNotNull { it.elementAtOrNull(i) }
            .let { result.addAll(it) }
    }

    return result
}

fun <T> List<T>.nullIfEmpty(): List<T>? {
    return takeIf { it.isNotEmpty() }
}

inline fun <reified T : Any> List<*>.firstOrNullAs(): T? = firstOrNull() as? T?
