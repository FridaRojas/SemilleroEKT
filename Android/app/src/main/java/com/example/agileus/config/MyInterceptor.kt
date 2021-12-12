package com.example.agileus.config

import com.example.agileus.config.InitialApplication.Companion.preferenciasGlobal
import okhttp3.Interceptor
import okhttp3.Response

//class MyInterceptor(val token: String, val tokenValue: String) : Interceptor {
class MyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        request.header("tokenAuth", preferenciasGlobal.recuperarTokenAuth())
        return chain.proceed(request.build())
    }
}