package com.example.agileus.config


import com.example.agileus.utils.Constantes.URL_BASE1
import com.example.agileus.utils.Constantes
import com.example.agileus.utils.Constantes.URL_REPORTES
import com.example.agileus.utils.Constantes.URL_BASE2
import com.example.agileus.utils.Constantes.URL_BASE_TAREAS
import com.example.agileus.utils.Constantes.URL_Tasks_Personas
import com.example.agileus.webservices.apis.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ConfigRetrofit {
    val URL_MESSAGE = Constantes.URL_ENVIAR_MENSAJE

    private var client = OkHttpClient.Builder().addInterceptor(MyInterceptor()).build()

    val URL_Login = Constantes.URL_Login

    fun cliente(tiempo: Long): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(tiempo, TimeUnit.SECONDS)
            .readTimeout(tiempo, TimeUnit.SECONDS)
            .writeTimeout(tiempo, TimeUnit.SECONDS)
            .addInterceptor(MyInterceptor())
            .build()
        return okHttpClient
    }

    fun obtenerConfiguracionRetofitMessage(): MessageApi {
        var mRetrofit = Retrofit.Builder()
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
             .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(TasksApi::class.java)
    }


    fun obtenerConfiguracionRetofitLogin(): LoginApi {

        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_Login)
            .addConverterFactory(GsonConverterFactory.create())
//            .client(cliente(5))
            .build()
        return mRetrofit.create(LoginApi::class.java)
    }

    fun obtenerConfiguracionRetrofitNivel(): NivelApi {

        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE_TAREAS)
            .addConverterFactory(GsonConverterFactory.create())
//            .client(cliente(5))
            .build()
        return mRetrofit.create(NivelApi::class.java)
    }

    fun obtenerConfiguracionRetofitPersonasTasks(): TasksApi {
        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_Tasks_Personas)
            .client(cliente(60))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(TasksApi::class.java)
    }



    fun obtenerConfiguracionRetofitBuzon2(): BuzonApi2 {

 //       http://3.144.86.49:8080/Servicios-0.0.1-SNAPSHOT/api/
        var mRetrofit = Retrofit.Builder()
            .baseUrl("http://3.144.86.49:8080/Servicios-0.0.1-SNAPSHOT/api/")
            .addConverterFactory(GsonConverterFactory.create())
  //          .client(client)
            .client(cliente(60))
            .build()
        return mRetrofit.create(BuzonApi2::class.java)
    }

    fun getConfigReportes(): ReportesApi{

        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_REPORTES)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(ReportesApi::class.java)

    }


    }
