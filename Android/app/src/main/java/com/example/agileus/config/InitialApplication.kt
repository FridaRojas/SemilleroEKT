package com.example.agileus.config

import android.app.Application
import com.example.agileus.ui.login.data.service.LoginApi
import com.example.agileus.webservices.apis.*
import com.example.agileus.webservices.apis.BuzonApi
import com.example.agileus.webservices.apis.BuzonApi2
import com.example.agileus.webservices.apis.MessageApi
import com.example.agileus.webservices.apis.TasksApi

class InitialApplication : Application() {

    companion object{
        lateinit var webServiceMessage: MessageApi
        lateinit var webServiceGlobalTasks: TasksApi
        lateinit var BroadcastServiceGlobalTasks: BuzonApi
        lateinit var BroadcastServiceGlobalTasks2: BuzonApi2
        lateinit var webServiceGlobalTasksPersonas: TasksApi

        //SharedPreferences
        lateinit var preferenciasGlobal: MySharedPreferences

        lateinit var LoginServiceGlobal : LoginApi

    }

    override fun onCreate() {
        super.onCreate()
        webServiceMessage = ConfigRetrofit().obtenerConfiguracionRetofitMessage()
        webServiceGlobalTasks = ConfigRetrofit().obtenerConfiguracionRetofitTasks()
        BroadcastServiceGlobalTasks=ConfigRetrofit().obtenerConfiguracionRetofitBuzon()

        LoginServiceGlobal=ConfigRetrofit().obtenerConfiguracionRetofitLogin()

        BroadcastServiceGlobalTasks2=ConfigRetrofit().obtenerConfiguracionRetofitBuzon2()

        webServiceGlobalTasksPersonas = ConfigRetrofit().obtenerConfiguracionRetofitPersonasTasks()

        //SharedPreferences
        preferenciasGlobal = MySharedPreferences(applicationContext)

    }

}