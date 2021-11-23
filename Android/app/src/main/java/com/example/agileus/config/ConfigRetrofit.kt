package com.example.agileus.config

import com.example.agileus.utils.Constantes
import com.example.agileus.webservices.apis.ConversationApi
import com.example.agileus.webservices.apis.MessageApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfigRetrofit {

   // val URL_BASE_CONVERSACION = Constantes.URL_BASE_CONV
    val URL_BASE_CONTACTS = "http://10.97.5.252:3040/api/"
  //  val URL_MESSAGE = Constantes.URL_ENVIAR_MENSAJE
  //  val URL_BASE_CONVERSACION = "https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/"

 /*   fun obtenerConfiguracionRetofit(): ConversationApi {
        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE_CONVERSACION)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(ConversationApi::class.java)
    }

  */

    fun obtenerConfiguracionRetofitListaDeContactos(): ConversationApi {
        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE_CONTACTS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(ConversationApi::class.java)
    }


/*
   fun obtenerConfiguracionRetofitMessage(): MessageApi {
        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_MESSAGE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(MessageApi::class.java)
    }


*/
}