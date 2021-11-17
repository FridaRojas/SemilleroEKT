package com.example.agileus.config

import android.app.Application
import com.example.agileus.webservices.ConversationApi

class InitialApplication : Application() {

    companion object{
        lateinit var webServiceGlobal:ConversationApi
    }
    override fun onCreate() {
        super.onCreate()

        //Este objeto ya puede ser accedido en cualquier parte dela app
        webServiceGlobal = ConfigRetrofit().obtenerConfiguracionRetofit()
    }

}