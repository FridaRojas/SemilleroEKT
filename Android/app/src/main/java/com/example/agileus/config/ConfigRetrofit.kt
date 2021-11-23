package com.example.agileus.config

import com.example.agileus.utils.Constantes
import com.example.agileus.webservices.apis.ConversationApi
import com.example.agileus.webservices.apis.MessageApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfigRetrofit {

   val URL_BASE_CONVERSACION = "http://10.97.5.252:3040/api/"
    val URL_BASE_CONTACTS = "http://10.97.5.252:3040/api/"
   val URL_MESSAGE = "http://10.97.5.252:3040/api/"
  //  val URL_BASE_CONVERSACION = "https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/"
    // val URL_BASE = "http://10.97.3.21:6060/servicio/"
    //val URL_BASE = "https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/"

    //todo Falta editar el url para las tareas
    val URL_BASE_TAREAS =
        "http://10.97.5.172:2021/api/"

    fun obtenerConfiguracionRetofit(): ConversationApi {
        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE_CONVERSACION)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(ConversationApi::class.java)
    }



    fun obtenerConfiguracionRetofitListaDeContactos(): ConversationApi {
        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE_CONTACTS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(ConversationApi::class.java)
    }



   fun obtenerConfiguracionRetofitMessage(): MessageApi {
        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_MESSAGE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(MessageApi::class.java)
    }



    fun obtenerConfiguracionRetofitTasks(): TasksApi {
        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE_TAREAS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(TasksApi::class.java)
    }


}