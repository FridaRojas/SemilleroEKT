package com.example.agileus.config

import com.example.agileus.webservices.apis.ConversationApi
import com.example.agileus.webservices.apis.TasksApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfigRetrofit {

    // val URL_BASE = "http://10.97.3.21:6060/servicio/"
    val URL_BASE = "https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/"

    //todo Falta editar el url para las tareas
    val URL_BASE_TAREAS =
        "10.97.1.64:2021/api/"

    fun obtenerConfiguracionRetofit(): ConversationApi {
        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(ConversationApi::class.java)
    }

    fun obtenerConfiguracionRetofitTasks(): TasksApi {
        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(TasksApi::class.java)
    }

}