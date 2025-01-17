package com.cosmos.stealth.core.model.api

/**
 * Pagination key
 *
 */
data class AfterKey (
    val value: Any
)

val AfterKey?.string: String?
    get() = this?.value as? String

val AfterKey?.int: Int?
    get() = this?.value as? Int
