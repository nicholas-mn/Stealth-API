package com.cosmos.stealth.core.network.util.extension

import com.cosmos.stealth.core.network.util.Resource

inline fun <T, R> Resource<T>.map(map: (T) -> R): Resource<R> {
    return when (this) {
        is Resource.Success -> Resource.Success(map(data))
        is Resource.Error -> this
        is Resource.Exception -> this
    }
}
