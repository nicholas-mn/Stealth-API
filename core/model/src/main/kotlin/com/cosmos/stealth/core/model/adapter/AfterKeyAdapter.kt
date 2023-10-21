package com.cosmos.stealth.core.model.adapter

import com.cosmos.stealth.core.model.api.AfterKey
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.ToJson

class AfterKeyAdapter {
    @FromJson
    fun fromJson(reader: JsonReader): AfterKey {
        val value: Any? = when (reader.peek()) {
            JsonReader.Token.STRING -> reader.nextString()
            JsonReader.Token.NUMBER -> {
                // Allow only integer values and prevent inputs such as 123.0
                val stringValue = reader.nextString()
                try { Integer.parseInt(stringValue) } catch (e: NumberFormatException) { null }
            }
            else -> null
        }

        check(value != null) { ERROR_MESSAGE }

        return AfterKey(value)
    }

    @ToJson
    fun toJson(key: AfterKey): Any {
        check(key.value is String || key.value is Int) { ERROR_MESSAGE }
        return key.value
    }

    companion object {
        private const val ERROR_MESSAGE = "Value must be a string or an integer"
    }
}
