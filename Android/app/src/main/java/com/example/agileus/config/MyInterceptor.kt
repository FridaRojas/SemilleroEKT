package com.example.agileus.config

import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        request.header("token_sesion", "192f81c6b619123d3ea563423ed278b4bdf2c8f432fd2f54c3d1f443f729e6d9")
        return chain.proceed(request.build())
    }
}