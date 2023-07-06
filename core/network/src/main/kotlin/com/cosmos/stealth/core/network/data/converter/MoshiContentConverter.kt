package com.cosmos.stealth.core.network.data.converter

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import io.ktor.http.ContentType
import io.ktor.http.content.OutgoingContent
import io.ktor.http.content.TextContent
import io.ktor.http.withCharsetIfNeeded
import io.ktor.serialization.Configuration
import io.ktor.serialization.ContentConverter
import io.ktor.serialization.JsonConvertException
import io.ktor.util.reflect.TypeInfo
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.charsets.Charset
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source

/**
 * Inspired by [this project](https://github.com/plannigan/ktor-moshi)
 */
class MoshiContentConverter(
    private val moshi: Moshi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ContentConverter {

    override suspend fun serializeNullable(
        contentType: ContentType,
        charset: Charset,
        typeInfo: TypeInfo,
        value: Any?
    ): OutgoingContent {
        return TextContent(
            moshi.adapter<Any?>(typeInfo.reifiedType).toJson(value),
            contentType.withCharsetIfNeeded(charset)
        )
    }

    override suspend fun deserialize(charset: Charset, typeInfo: TypeInfo, content: ByteReadChannel): Any? {
        try {
            return withContext(ioDispatcher) {
                val source = content.toInputStream().source().buffer()
                moshi.adapter<Any?>(typeInfo.reifiedType).fromJson(source)
            }
        } catch (e: JsonDataException) {
            throw JsonConvertException("Unable to convert JSON", e)
        } catch (e: JsonEncodingException) {
            throw JsonConvertException("Invalid JSON", e)
        }
    }
}

fun Configuration.moshi(moshiContentConverter: MoshiContentConverter) {
    register(ContentType.Application.Json, moshiContentConverter)
}
