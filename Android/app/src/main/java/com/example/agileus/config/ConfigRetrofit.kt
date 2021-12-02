package com.example.agileus.config


import com.example.agileus.webservices.apis.BuzonApi
import com.example.agileus.utils.Constantes
import com.example.agileus.utils.Constantes.URL_BASE2
import com.example.agileus.utils.Constantes.URL_BASE_TAREAS
import com.example.agileus.utils.cliente
import com.example.agileus.webservices.apis.MessageApi
import com.example.agileus.webservices.apis.TasksApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit



fun cliente(tiempo:Long): OkHttpClient {
    val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(tiempo, TimeUnit.SECONDS)
        .readTimeout(tiempo, TimeUnit.SECONDS)
        .writeTimeout(tiempo, TimeUnit.SECONDS)
        .build()
    return okHttpClient
}


class ConfigRetrofit {

    val URL_MESSAGE = Constantes.URL_ENVIAR_MENSAJE

   fun obtenerConfiguracionRetofitMessage(): MessageApi {
       val mRetrofit = Retrofit.Builder()
            .baseUrl(URL_MESSAGE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return mRetrofit.create(MessageApi::class.java)
    }

    fun obtenerConfiguracionRetofitTasks(): TasksApi{

        /*val clientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(loggingInterceptor)*/

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
            .addConverterFactory(GsonConverterFactory.create())
            .client(cliente(60))
            .build()
        return mRetrofit.create(BuzonApi::class.java)
    }

    fun obtenerConfiguracionRetofitBuzon2(): BuzonApi2 {

        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE3)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return mRetrofit.create(BuzonApi2::class.java)
    }

}