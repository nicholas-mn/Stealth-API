package com.cosmos.stealth.services.reddit.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class JsonInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url

        val newUrl = url.newBuilder()
            .addPathSegment(".json")
            .build()

        val newRequest = request.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}
