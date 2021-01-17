package com.example.academyproject.network

import okhttp3.Interceptor
import okhttp3.Response

private const val API_KEY = "e2bf566ba7a7bfe4b4b9312f96988252"

class ApiKeyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val newUrl = original.url.newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .build()
        val request = original.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(request)
    }
}