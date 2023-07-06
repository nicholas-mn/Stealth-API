package com.cosmos.stealth.core.network.data.annotation

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Path(val value: String, val encoded: Boolean = false)
