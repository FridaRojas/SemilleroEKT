package com.example.agileus.webservices.apis

import com.example.agileus.models.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ReportesApi {

    //@GET("ruta/{id}")
    //@GET("Task.json?alt=media&token=bb6a2086-2e39-411a-8385-2294dabcc2d5")

    //@GET("tareas.json?alt=media&token=4aac6ac6-b294-4366-896b-59ac573f15ab")
    //fun getDatosReporteTareas(): Call<ArrayList<DatosTareas>>
    //@GET("TareasFechas.json?alt=media&token=70ef0428-836f-421f-a9ef-cc6563b64819")
    @GET("tareas/obtenerTareasQueLeAsignaronPorId/{id}")        //Listo
    fun getDatosReporteTareas(@Path("id") idBusqueda:String): Call<TaskListByID>
    //fun getDatosReporteTareas(): Call<ArrayList<Tasks>>
    //@GET("tareas/obtenerTareasQueLeAsignaronPorId/618b05c12d3d1d235de0ade0")
    //fun getDatosReporteTareas(): Call<TaskListByID>
    //fun getDatosReporteTareas(): Call<ArrayList<DatosTareas>>

    //@GET("Messages.json?alt=media&token=03022225-583c-4114-a056-ce4964b1a928")
    @GET("mensajes/listarMensajesRecividos/{idUser}/{idSearch}")
    fun getDatosReporteMensajes(@Path("idUser") idUsuario:String, @Path("idSearch") idBusqueda:String): Call<conversartionListByID>

    @GET("broadCast/mostrarMensajesporID/{idUser}/{idSearch}")
    fun getDatosRespuestasBroadcast(@Path("idUser") idUsuario:String, @Path("idSearch") idBusqueda:String): Call<ArrayList<DatosBroadCast>>

    //Busqueda por Id de jefe
    @GET("user/findByBossId/{idsuperiorInmediato}")
    fun getListSubContacts(@Path("idsuperiorInmediato") idsuperiorInmediato: String):Call<EmployeeListByBossID>
    //fun getListSubContacts(@Path("idsuperiorInmediato") idsuperiorInmediato: String):Call<ArrayList<Contacts>>


}