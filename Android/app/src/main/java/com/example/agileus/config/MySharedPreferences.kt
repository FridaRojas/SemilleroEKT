package com.example.agileus.config

import com.example.agileus.models.Contacts
import com.example.agileus.utils.Constantes

class MySharedPreferences {

    companion object reportesGlobales{
        var idUsuario = Constantes.id
        var idUsuarioEstadisticas = Constantes.id
        var opcionFiltro = 0
        var fechaIniEstadisticas = "1970-01-01T00:00:00.000+00:00"
        var fechaFinEstadisticas = Constantes.date.toString()
        var empleadoUsuario = emptyList<Contacts>()
        var tipo_grafica:Int=0
        var vista:Int=0
    }

}
