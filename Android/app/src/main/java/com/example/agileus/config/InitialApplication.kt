package com.example.agileus.config

import android.app.Application
import com.example.agileus.webservices.apis.ConversationApi
import com.example.agileus.webservices.apis.ReportesApi

class InitialApplication : Application() {

    companion object{
        lateinit var webServiceGlobal: ConversationApi
        lateinit var webServiceGlobalReportes: ReportesApi

    }

    override fun onCreate() {
        super.onCreate()

        //Este objeto ya puede ser accedido en cualquier parte dela app
        webServiceGlobal = ConfigRetrofit().obtenerConfiguracionRetofit()
        webServiceGlobalReportes = ConfigRetrofit().getconfigreportesTareas()

    }

}