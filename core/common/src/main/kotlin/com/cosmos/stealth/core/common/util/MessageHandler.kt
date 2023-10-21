package com.cosmos.stealth.core.common.util

import java.text.MessageFormat
import java.util.Locale
import java.util.MissingResourceException
import java.util.ResourceBundle

object MessageHandler {

    // moduleName.i18n.strings
    private const val STRINGS_PATH = "%s.i18n.strings"

    fun getString(locale: Locale, key: String, vararg args: Any?): String {
        val module = key.getModule()
        val bundle = try {
            ResourceBundle.getBundle(STRINGS_PATH.format(module), locale)
        } catch (_: MissingResourceException) {
            return ""
        }

        val string = bundle.getString(key)

        return when {
            args.isNotEmpty() -> MessageFormat.format(string, *args)
            else -> string
        }
    }

    private fun String.getModule(): String {
        return this.split('.')[0]
    }
}
