package com.example.agileus.config

import android.app.Application
import com.example.agileus.webservices.apis.ConversationApi
import com.example.agileus.webservices.apis.MessageApi
import com.example.agileus.webservices.apis.TasksApi

class InitialApplication : Application() {

    companion object{
        lateinit var webServiceConversation: ConversationApi
        lateinit var webServiceMessage:MessageApi
        lateinit var webServiceGlobalTasks: TasksApi
        lateinit var webServiceGlobal:ConversationApi

    }

    override fun onCreate() {
        super.onCreate()
        //Este objeto ya puede ser accedido en cualquier parte dela app
       webServiceConversation = ConfigRetrofit().obtenerConfiguracionRetofit()
       webServiceMessage = ConfigRetrofit().obtenerConfiguracionRetofitMessage()
        webServiceGlobal = ConfigRetrofit().obtenerConfiguracionRetofitBuzon()

        //Este objeto ya puede ser accedido en cualquier parte de la app
         webServiceGlobalTasks = ConfigRetrofit().obtenerConfiguracionRetofitTasks()

    }

}