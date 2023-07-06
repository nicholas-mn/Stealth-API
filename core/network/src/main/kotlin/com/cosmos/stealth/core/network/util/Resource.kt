package com.cosmos.stealth.core.network.util

sealed class Resource<out T> {

    data class Success<out T>(val data: T) : Resource<T>()

    data class Error(val code: Int, val message: String) : Resource<Nothing>()

    data class Exception(val throwable: Throwable) : Resource<Nothing>()
}
