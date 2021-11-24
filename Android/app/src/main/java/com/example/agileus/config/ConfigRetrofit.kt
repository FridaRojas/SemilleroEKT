package com.example.agileus.config


import com.example.agileus.utils.Constantes.URL_BASE1
import com.example.agileus.utils.Constantes.URL_BASE2
import com.example.agileus.webservices.apis.BuzonApi
import com.example.agileus.utils.Constantes.URL_BASE_TAREAS
import com.example.agileus.webservices.apis.ConversationApi
import com.example.agileus.webservices.apis.TasksApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfigRetrofit {

    fun obtenerConfiguracionRetofit(): ConversationApi {
        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE1)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(ConversationApi::class.java)
    }

    fun obtenerConfiguracionRetofitTasks(): TasksApi {
        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE_TAREAS)
            .addConverterFactory(GsonConverterFactory.create())
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