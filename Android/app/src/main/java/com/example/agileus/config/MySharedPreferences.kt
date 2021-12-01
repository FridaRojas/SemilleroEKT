package com.example.agileus.config

import com.example.agileus.models.Contacts
import com.example.agileus.utils.Constantes

class MySharedPreferences {

    companion object reportesGlobales{
        var idUsuario = "618e8743c613329636a769aa"
        var idUsuarioEstadisticas = Constantes.id
        var id_broadcast="61a101db174bcf469164d2fd"
        var fechaIniEstadisticas = "1970-01-01T00:00:00.000+00:00"
        var fechaFinEstadisticas = Constantes.zonedDate.toString()
        var empleadoUsuario = emptyList<Contacts>()
        var tipo_grafica:Int=0
        var vista:Int=0
    }

}
