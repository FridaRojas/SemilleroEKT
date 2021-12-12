package com.example.agileus.config

import android.app.Application
import com.example.agileus.webservices.apis.*

class InitialApplication : Application() {

    companion object {
        lateinit var webServiceMessage: MessageApi
        lateinit var webServiceGlobalTasks: TasksApi
        lateinit var BroadcastServiceGlobalTasks: BuzonApi
        lateinit var BroadcastServiceGlobalTasks2: BuzonApi2
        lateinit var webServiceGlobalTasksPersonas: TasksApi
       lateinit var webServiceGlobalTasksPrueba: TasksApi
        lateinit var webServiceGlobalReportes: ReportesApi
        lateinit var webServiceGlobalReportesBroadCast: ReportesApi
        lateinit var webServiceGlobalReportesTareas: ReportesApi
        lateinit var webServiceGlobalNivel : NivelApi
       //lateinit var webServiceGlobalTasksPrueba: TasksApi

        //SharedPreferences
        lateinit var preferenciasGlobal: MySharedPreferences
        lateinit var preferenciasToken: TokenSharedPreferences

        lateinit var LoginServiceGlobal : LoginApi

    }

    override fun onCreate() {
        super.onCreate()
        webServiceMessage = ConfigRetrofit().obtenerConfiguracionRetofitMessage()
        webServiceGlobalTasks = ConfigRetrofit().obtenerConfiguracionRetofitTasks()
//        BroadcastServiceGlobalTasks=ConfigRetrofit().obtenerConfiguracionRetofitBuzon()

        LoginServiceGlobal=ConfigRetrofit().obtenerConfiguracionRetofitLogin()

        BroadcastServiceGlobalTasks2=ConfigRetrofit().obtenerConfiguracionRetofitBuzon2()

  //      BroadcastServiceGlobalTasks = ConfigRetrofit().obtenerConfiguracionRetofitBuzon()
        webServiceGlobalTasksPersonas = ConfigRetrofit().obtenerConfiguracionRetofitPersonasTasks()
      //  webServiceGlobalTasksPrueba = ConfigRetrofit().obtenerConfiguracionRetofitTasksPrueba()

        webServiceGlobalReportes = ConfigRetrofit().getConfigReportes()
        webServiceGlobalNivel = ConfigRetrofit().obtenerConfiguracionRetrofitNivel()

        //SharedPreferences
        preferenciasGlobal = MySharedPreferences(applicationContext)
        preferenciasToken = TokenSharedPreferences(applicationContext)
    }

}