package com.example.agileus.config


import com.example.agileus.webservices.apis.BuzonApi
import com.example.agileus.utils.Constantes.URL_Tasks_Personas
import com.example.agileus.utils.Constantes
import com.example.agileus.utils.Constantes.URL_BASE2
import com.example.agileus.utils.Constantes.URL_BASE_TAREAS
import com.example.agileus.utils.cliente
import com.example.agileus.webservices.apis.MessageApi
import com.example.agileus.webservices.apis.TasksApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Request


class ConfigRetrofit {

    val URL_MESSAGE = Constantes.URL_ENVIAR_MENSAJE

    fun obtenerConfiguracionRetofitMessage(): MessageApi {
        val mRetrofit = Retrofit.Builder()
            .baseUrl(URL_MESSAGE)
            .client(cliente(60))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return mRetrofit.create(MessageApi::class.java)
    }

    fun obtenerConfiguracionRetofitTasks(): TasksApi {

        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE_TAREAS)
            // .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(TasksApi::class.java)
    }

    fun obtenerConfiguracionRetofitPersonasTasks(): TasksApi {
        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_Tasks_Personas)
            .client(cliente(60))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(TasksApi::class.java)
    }

    fun obtenerConfiguracionRetofitBuzon(): BuzonApi {

        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE2)
            .client(cliente(60))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return mRetrofit.create(BuzonApi::class.java)
    }


}