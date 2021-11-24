package com.example.agileus.config

import android.app.Application
import com.example.agileus.webservices.apis.ConversationApi
import com.example.agileus.webservices.apis.TasksApi

class InitialApplication : Application() {

    companion object {
        lateinit var webServiceGlobal: ConversationApi
        lateinit var webServiceGlobalTasks: TasksApi
        lateinit var webServiceGlobalTasksPersonas: TasksApi

    }

    override fun onCreate() {
        super.onCreate()

        //Este objeto ya puede ser accedido en cualquier parte de la app
        webServiceGlobal = ConfigRetrofit().obtenerConfiguracionRetofit()
        webServiceGlobalTasks = ConfigRetrofit().obtenerConfiguracionRetofitTasks()
        webServiceGlobalTasksPersonas = ConfigRetrofit().obtenerConfiguracionRetofitPersonasTasks()

    }

}