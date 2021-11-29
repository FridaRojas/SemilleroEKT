package com.example.agileus.config

class MySharedPreferences {

    companion object reportesGlobales{
        lateinit var idUsuario: String
        lateinit var idUsuarioEstadisticas: String
        lateinit var fechaInicioEstadisticas: String
        lateinit var fechaFinEstadisticas: String
        var tipo_grafica:Int=0
        var vista:Int=0
    }
}
