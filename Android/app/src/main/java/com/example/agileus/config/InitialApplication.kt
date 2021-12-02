package com.example.agileus.config

import android.app.Application
import com.example.agileus.webservices.apis.BuzonApi
import com.example.agileus.webservices.apis.MessageApi
import com.example.agileus.webservices.apis.ReportesApi
import com.example.agileus.webservices.apis.TasksApi

class InitialApplication : Application() {

    companion object{
        lateinit var webServiceMessage: MessageApi
        lateinit var webServiceGlobalTasks: TasksApi
        lateinit var BroadcastServiceGlobalTasks: BuzonApi
        lateinit var webServiceGlobalReportes: ReportesApi
        lateinit var webServiceGlobalReportesBroadCast: ReportesApi

    }

    override fun onCreate() {
        super.onCreate()
        webServiceMessage = ConfigRetrofit().obtenerConfiguracionRetofitMessage()
        webServiceGlobalTasks = ConfigRetrofit().obtenerConfiguracionRetofitTasks()
        BroadcastServiceGlobalTasks = ConfigRetrofit().obtenerConfiguracionRetofitBuzon()
        webServiceGlobalReportes = ConfigRetrofit().getConfigReportes()
        webServiceGlobalReportesBroadCast = ConfigRetrofit().getBroadCastReportes()
    }

}