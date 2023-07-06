package com.cosmos.stealth.core.common.data.mapper

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class Mapper<From, Context, To>(protected val defaultDispatcher: CoroutineDispatcher) {

    protected abstract suspend fun toEntity(from: From, context: Context? = null): To

    protected open suspend fun toEntities(from: List<From>, context: Context? = null): List<To> {
        return from.map { toEntity(it, context) }
    }

    protected open suspend fun fromEntity(from: To): From {
        throw UnsupportedOperationException()
    }

    protected open suspend fun fromEntities(from: List<To>): List<From> {
        return from.map { fromEntity(it) }
    }

    suspend fun dataToEntity(from: From, context: Context? = null): To = withContext(defaultDispatcher) {
        toEntity(from, context)
    }

    suspend fun dataToEntities(from: List<From>, context: Context? = null): List<To> = withContext(defaultDispatcher) {
        toEntities(from, context)
    }

    suspend fun dataFromEntity(from: To): From = withContext(defaultDispatcher) {
        fromEntity(from)
    }

    suspend fun dataFromEntities(from: List<To>): List<From> = withContext(defaultDispatcher) {
        fromEntities(from)
    }
}
