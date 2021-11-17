package com.example.agileus.config

import com.example.agileus.webservices.apis.ConversationApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfigRetrofit {

    val URL_BASE = "http://10.97.3.21:6060/servicio/"
    //val URL_BASE = "https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/"

    fun obtenerConfiguracionRetofit(): ConversationApi {
        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(ConversationApi::class.java)
    }

}