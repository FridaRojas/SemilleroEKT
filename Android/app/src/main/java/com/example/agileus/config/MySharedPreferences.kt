package com.example.agileus.config

import android.content.Context
import com.example.agileus.models.UserMessageDetailReport
import com.example.agileus.models.UserTaskDetailReport
import com.example.agileus.utils.Constantes

class MySharedPreferences(contexto: Context) {

    companion object{
        val TOKEN_KEY = "TOKEN_KEY"
        val SESSION_TOKEN = "TOKEN"
    }

    val sharedPreferences = contexto.getSharedPreferences(SESSION_TOKEN, Context.MODE_PRIVATE)

    fun recuperarToken() : String{
        return sharedPreferences.getString(TOKEN_KEY, "")!!
    }
/*

    companion object reportesGlobales{
        var idUsuario = "618e8743c613329636a769aa"
        var idUsuarioEstadisticas = Constantes.id
        var opcionFiltro = 0
        var fechaIniCustomEstadisticas = "1970-01-01T00:00:00.000+00:00"
        var fechaEstadisticas = Constantes.date.toString()
        var id_broadcast="61a101db174bcf469164d2fd"
        var fechaIniEstadisticas = "1900-01-01T00:00:00.000+00:00"
        var fechaFinEstadisticas = "2100-01-01T00:00:00.000+00:00"
        var empleadoUsuario = emptyList<UserMessageDetailReport>()
        var dataEmpleadoUsuario = emptyList<UserTaskDetailReport>()
        var tipo_grafica:Int=0
        var vista:Int=0
    }
 */
}