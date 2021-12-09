package com.example.agileus.webservices.apis

import com.example.agileus.models.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ReportesApi {
    @GET("tareas/obtenerTareasQueLeAsignaronPorId/{id}")        //Listo
    fun getDatosReporteTareas(@Path("id") idBusqueda:String): Call<TaskListByID>

    @GET("mensajes/listarMensajesRecividos/{idUser}/{idSearch}")
    fun getDatosReporteMensajes(@Path("idUser") idUsuario:String, @Path("idSearch") idBusqueda:String): Call<conversartionListByID>

    @GET("broadCast/mostrarMensajesporID/{idUser}/{idSearch}")
    fun getDatosRespuestasBroadcast(@Path("idUser") idUsuario:String, @Path("idSearch") idBusqueda:String): Call<BroadcastByID>

    @GET("user/findByBossId/{idsuperiorInmediato}")
    fun getListSubContacts(@Path("idsuperiorInmediato") idsuperiorInmediato: String):Call<EmployeeListByBossID>

}