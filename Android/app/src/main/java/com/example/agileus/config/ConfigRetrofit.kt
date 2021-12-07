package com.example.agileus.config


import com.example.agileus.utils.Constantes.URL_BASE1
import com.example.agileus.webservices.apis.BuzonApi
import com.example.agileus.utils.Constantes
import com.example.agileus.ui.login.data.service.LoginApi
import com.example.agileus.utils.Constantes.URL_BASE2
import com.example.agileus.utils.Constantes.URL_BASE3
import com.example.agileus.utils.Constantes.URL_Tasks_Personas
import com.example.agileus.webservices.apis.BuzonApi2
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
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Request


class ConfigRetrofit {
    val URL_MESSAGE = Constantes.URL_ENVIAR_MENSAJE
    //val URL_LOGIN = Constantes.URL_LOGIN
    val URL_Login = Constantes.URL_Login

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

    fun obtenerConfiguracionRetofitTasks(): TasksApi {

        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE_TAREAS)
            // .client(clientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(TasksApi::class.java)
    }


    fun obtenerConfiguracionRetofitLogin(): LoginApi {

        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_Login)
            .addConverterFactory(GsonConverterFactory.create())
            .client(cliente(60))
            .build()
        return mRetrofit.create(LoginApi::class.java)
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