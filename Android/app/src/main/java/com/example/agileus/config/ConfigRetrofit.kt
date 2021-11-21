package com.example.agileus.config


import com.example.agileus.utils.Constantes.URL_BASE
import com.example.agileus.utils.Constantes.URL_BASE1
import com.example.agileus.webservices.apis.ConversationApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfigRetrofit {

   // val URL_BASE = "http://10.97.3.21:6060/servicio/"


    fun obtenerConfiguracionRetofit(): ConversationApi {
        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE1)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(ConversationApi::class.java)
    }

}