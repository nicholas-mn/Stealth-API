package com.cosmos.stealth.core.network.util

import com.cosmos.stealth.core.network.data.annotation.GET
import com.cosmos.stealth.core.network.data.annotation.Header
import com.cosmos.stealth.core.network.data.annotation.POST
import com.cosmos.stealth.core.network.data.annotation.Path
import com.cosmos.stealth.core.network.data.annotation.Query
import kotlin.reflect.KFunction
import kotlin.reflect.full.valueParameters

fun KFunction<*>.getEndpoint(): String {
    check(annotations.isNotEmpty()) { "API method should be annotated" }

    val supportedType = listOf(GET::class.java, POST::class.java)

    val methodType = annotations.firstOrNull { supportedType.contains(it.annotationClass.java) }

    return when (methodType) {
        is GET -> methodType.value
        is POST -> methodType.value
        else -> error("Unknown API method type $methodType")
    }
}

fun KFunction<*>.getPathParameter(index: Int): String {
    return valueParameters
        .filter { param -> param.annotations.any { annotation -> annotation is Path } }
        .map { param -> param.annotations.firstOrNull { annotation -> annotation is Path } }
        .map { (it as Path).value }
        .getOrNull(index)
        ?: error("Path parameter not found at index $index for function ${this.name}")
}

fun KFunction<*>.getQueryParameter(index: Int): String {
    return valueParameters
        .filter { param -> param.annotations.any { annotation -> annotation is Query } }
        .map { param -> param.annotations.firstOrNull { annotation -> annotation is Query } }
        .map { (it as Query).value }
        .getOrNull(index)
        ?: error("Query parameter not found at index $index for function ${this.name}")
}

fun KFunction<*>.getHeader(index: Int): String {
    return valueParameters
        .filter { param -> param.annotations.any { annotation -> annotation is Header } }
        .map { param -> param.annotations.firstOrNull { annotation -> annotation is Header } }
        .map { (it as Header).value }
        .getOrNull(index)
        ?: error("Header not found at index $index for function ${this.name}")
}
