package com.example.agileus.config

import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        request.header("tokenAuth", "")
        return chain.proceed(request.build())
    }
}