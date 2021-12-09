package com.example.agileus.config

import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        request.header("tokenAuth", "c9acb094036a82eb6dbac287b6dc437b87f25c95ee954db469a4c424eacdcaba")
        return chain.proceed(request.build())
    }
}