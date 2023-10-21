package com.cosmos.stealth.core.common.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getDateFromString(pattern: String, string: String): Date? {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.parse(string)
}
