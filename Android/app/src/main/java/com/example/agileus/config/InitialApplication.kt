package com.example.agileus.config

import android.app.Application
import com.example.agileus.webservices.apis.BuzonApi
import com.example.agileus.webservices.apis.MessageApi
import com.example.agileus.webservices.apis.TasksApi

class InitialApplication : Application() {

    companion object {
        lateinit var webServiceMessage: MessageApi
        lateinit var webServiceGlobalTasks: TasksApi
        lateinit var BroadcastServiceGlobalTasks: BuzonApi
        lateinit var webServiceGlobalTasksPersonas: TasksApi
       // lateinit var webServiceGlobalTasksPrueba: TasksApi

        //SharedPreferences
        lateinit var preferenciasGlobal: MySharedPreferences

    }

    override fun onCreate() {
        super.onCreate()
        webServiceMessage = ConfigRetrofit().obtenerConfiguracionRetofitMessage()
        webServiceGlobalTasks = ConfigRetrofit().obtenerConfiguracionRetofitTasks()
        BroadcastServiceGlobalTasks = ConfigRetrofit().obtenerConfiguracionRetofitBuzon()
        webServiceGlobalTasksPersonas = ConfigRetrofit().obtenerConfiguracionRetofitPersonasTasks()
       // webServiceGlobalTasksPrueba = ConfigRetrofit().obtenerConfiguracionRetofitTasksPrueba()


        //SharedPreferences
        preferenciasGlobal = MySharedPreferences(applicationContext)

    }

}