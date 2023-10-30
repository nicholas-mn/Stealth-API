package com.cosmos.stealth.core.network.util.extension

import okhttp3.Cache
import okhttp3.OkHttpClient
import java.nio.file.Files
import java.nio.file.Paths

private const val MAX_SIZE = 50L * 1024L * 1024L // 500MiB

fun OkHttpClient.Builder.cache(module: String, maxSize: Long = MAX_SIZE): OkHttpClient.Builder {
    return cache(Cache(Files.createDirectories(Paths.get("cache/$module")).toFile(), maxSize))
}
