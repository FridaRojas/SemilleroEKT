package com.example.agileus.config


import com.example.agileus.utils.Constantes.URL_BASE1
import com.example.agileus.webservices.apis.BuzonApi
import com.example.agileus.utils.Constantes
import com.example.agileus.utils.Constantes.URL_BASE2
import com.example.agileus.webservices.apis.MessageApi
import com.example.agileus.webservices.apis.TasksApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfigRetrofit {
    val URL_MESSAGE = Constantes.URL_ENVIAR_MENSAJE

    //todo Falta editar el url para las tareas
    val URL_BASE_TAREAS =
        "http://10.97.5.172:2021/api/"


   fun obtenerConfiguracionRetofitMessage(): MessageApi {
        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_MESSAGE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return mRetrofit.create(MessageApi::class.java)
    }

    fun obtenerConfiguracionRetofitTasks(): TasksApi{
        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE_TAREAS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(TasksApi::class.java)
    }

    fun obtenerConfiguracionRetofitBuzon(): BuzonApi {

        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE2)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(BuzonApi::class.java)
    }


}