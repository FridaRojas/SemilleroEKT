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
    var client = OkHttpClient.Builder().apply {
        addInterceptor(MyInterceptor())
    }.build()

    fun obtenerConfiguracionRetofitMessage(): MessageApi {
        val mRetrofit = Retrofit.Builder()
            .baseUrl(URL_MESSAGE)
            .client(cliente(60))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return mRetrofit.create(MessageApi::class.java)
    }

    fun obtenerConfiguracionRetofitTasks(): TasksApi {
        /*   var http = OkHttpClient().newBuilder().addInterceptor(
               Interceptor { chain ->
                   val requestBuilder: Request.Builder = chain.request().newBuilder()
                   requestBuilder.header("token_sesion", "12345")
                   chain.proceed(requestBuilder.build())

               }).build()*/


        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE_TAREAS)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(TasksApi::class.java)
    }

    /*  fun obtenerConfiguracionRetofitTasksPrueba(): TasksApi {
          var http = OkHttpClient().newBuilder().addInterceptor(
              Interceptor { chain ->
                  val requestBuilder: Request.Builder = chain.request().newBuilder()
                  requestBuilder.header("token_sesion", "12345")
                  chain.proceed(requestBuilder.build())
              }).build()

          var mRetrofit = Retrofit.Builder()
              .baseUrl("http://10.97.3.24:3040/api/")
              .client(http)
              .addConverterFactory(GsonConverterFactory.create())
              .build()

          return mRetrofit.create(TasksApi::class.java)
      }*/


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