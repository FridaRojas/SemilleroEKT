package com.example.agileus.config

import com.example.agileus.models.UserMessageDetailReports
import com.example.agileus.models.UserTaskListDetail
import com.example.agileus.utils.Constantes

class MySharedPreferences {

    companion object reportesGlobales{
        var idUsuario = "618e8743c613329636a769aa"
        var idUsuarioEstadisticas = Constantes.id
        var opcionFiltro = 0
        var fechaIniCustomEstadisticas = "1970-01-01T00:00:00.000+00:00"
        var fechaEstadisticas = Constantes.date.toString()
        var id_broadcast="61a101db174bcf469164d2fd"
        var fechaIniEstadisticas = "1900-01-01T00:00:00.000+00:00"
        var fechaFinEstadisticas = "2100-01-01T00:00:00.000+00:00"
        var empleadoUsuario = ArrayList<UserMessageDetailReports>() //Objeto para recibir los datos de los mensajes de un usuario o grupo de usuarios
        var dataEmpleadoUsuario = ArrayList<UserTaskListDetail>() //Objeto para recibir los datos de las tareas de un usuario o grupo de usuarios
        var tipo_grafica:Int=0
        var vista:Int=0
    }

}
