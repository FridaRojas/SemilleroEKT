package com.example.agileus.config


import com.example.agileus.utils.Constantes.URL_BASE1
import com.example.agileus.webservices.apis.ReportesApi
import com.example.agileus.webservices.apis.BuzonApi
import com.example.agileus.utils.Constantes
import com.example.agileus.utils.Constantes.URL_REPORTES
import com.example.agileus.webservices.apis.LoginApi
import com.example.agileus.ui.login.data.service.LoginApi
import com.example.agileus.ui.login.ui.login.InicioSesionFragment.Companion.token
import com.example.agileus.utils.Constantes.URL_BASE2
import com.example.agileus.utils.Constantes.URL_BASE_TAREAS
import com.example.agileus.utils.Constantes.URL_Tasks_Personas
import com.example.agileus.webservices.apis.BuzonApi2
import com.example.agileus.webservices.apis.MessageApi
import com.example.agileus.webservices.apis.TasksApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ConfigRetrofit {
    val URL_MESSAGE = Constantes.URL_ENVIAR_MENSAJE

    private var client = OkHttpClient.Builder().addInterceptor(MyInterceptor()).build()


    //val URL_LOGIN = Constantes.URL_LOGIN
    val URL_Login = Constantes.URL_Login

    fun cliente(tiempo: Long): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(tiempo, TimeUnit.SECONDS)
            .readTimeout(tiempo, TimeUnit.SECONDS)
            .writeTimeout(tiempo, TimeUnit.SECONDS)
            .build()
        return okHttpClient
    }

    fun obtenerConfiguracionRetofitMessage(): MessageApi {
        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_MESSAGE)
            .client(client)
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
            // .client(clientBuilder.build())
             .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(TasksApi::class.java)
    }

    /*fun obtenerConfiguracionRetofitTasksPrueba(): TasksApi {


        *//*var http = OkHttpClient().newBuilder().addInterceptor(
            Interceptor { chain ->
                val requestBuilder: Request.Builder = chain.request().newBuilder()
                requestBuilder.header("token_sesion", "12345")
                chain.proceed(requestBuilder.build())
            }).build()*//*

        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_BASE_TAREAS)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(TasksApi::class.java)
    }*/


    fun obtenerConfiguracionRetofitLogin(): LoginApi {

        var mRetrofit = Retrofit.Builder()
            .baseUrl(URL_Login)
            .addConverterFactory(GsonConverterFactory.create())
//            .client(cliente(5))
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



    fun obtenerConfiguracionRetofitBuzon2(): BuzonApi2 {


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

    fun getBroadCastReportes(): ReportesApi{

        var mRetrofit = Retrofit.Builder()
            .baseUrl("http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(ReportesApi::class.java)

    }


    fun getTareasporId(): ReportesApi{

        var mRetrofit = Retrofit.Builder()
            .baseUrl("http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return mRetrofit.create(ReportesApi::class.java)

    }




    }
