package com.example.agileus.config


import com.example.agileus.utils.Constantes.URL_BASE
import com.example.agileus.utils.Constantes.URL_BASE1
import com.example.agileus.utils.Constantes.URL_BASE2
import com.example.agileus.webservices.apis.BuzonApi
import com.example.agileus.utils.Constantes.URL_BASE_TAREAS
import com.example.agileus.webservices.apis.ConversationApi
import com.example.agileus.webservices.apis.TasksApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder

import com.google.gson.Gson
import retrofit2.converter.scalars.ScalarsConverterFactory


class ConfigRetrofit {

    // val URL_BASE = "http://10.97.3.21:6060/servicio/"
    //val URL_BASE = "https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/"

    //todo Falta editar el url para las tareas
    val URL_BASE_TAREAS =
        "http://10.97.3.134:2021/api/"

    fun obtenerConfiguracionRetofit(): ConversationApi {
        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE1)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(ConversationApi::class.java)
    }

    fun obtenerConfiguracionRetofitTasks(): TasksApi {

        val gson = GsonBuilder()
            .setLenient()
            .create()


        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE_TAREAS)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        return mRetrofit.create(TasksApi::class.java)
    }

    fun obtenerConfiguracionRetofitBuzon(): BuzonApi {

        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE1)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(BuzonApi::class.java)
    }


}