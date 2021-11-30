package com.example.agileus.config

import com.example.agileus.utils.Constantes

class MySharedPreferences {

    companion object reportesGlobales{
        var idUsuario = Constantes.id
        var idUsuarioEstadisticas = Constantes.id
        lateinit var fechaInicioEstadisticas: String
        lateinit var fechaFinEstadisticas: String
        var tipo_grafica:Int=0
        var vista:Int=0
    }

}
