package com.example.agileus.config

import android.app.Application
import com.example.agileus.webservices.apis.ConversationApi
import com.example.agileus.webservices.apis.MessageApi

class InitialApplication : Application() {

    companion object{
        lateinit var webServiceConversation: ConversationApi
       lateinit var webServiceMessage:MessageApi
        lateinit var webServiceListContacts:ConversationApi
    }

    override fun onCreate() {
        super.onCreate()
        //Este objeto ya puede ser accedido en cualquier parte dela app
       webServiceConversation = ConfigRetrofit().obtenerConfiguracionRetofit()
       webServiceMessage = ConfigRetrofit().obtenerConfiguracionRetofitMessage()
        webServiceListContacts = ConfigRetrofit().obtenerConfiguracionRetofitListaDeContactos()
    }
}