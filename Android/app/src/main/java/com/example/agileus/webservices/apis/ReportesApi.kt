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
    @GET("taskByIDFinalModel.json?alt=media&token=e35282ae-48aa-404f-a462-b8ebdba598f5")
    fun getDatosReporteTareas(): Call<TaskListByID>
    //fun getDatosReporteTareas(): Call<ArrayList<Tasks>>

    //@GET("Messages.json?alt=media&token=03022225-583c-4114-a056-ce4964b1a928")
    @GET("Messages.json?alt=media&token=39312937-7454-48f1-aa82-5ba279dee438")
    fun getDatosReporteMensajes(): Call<ArrayList<Conversation>>


    @GET("api/broadCast/mostrarMensajesporID/{id}")
    fun getDatosRespuestasBroadcast(@Path("id") idBusqueda:String): Call<ArrayList<DatosBroadCast>>

    //Busqueda por Id de jefe
    @GET("usuarios_encontrados.json?alt=media&token=92614c22-70fc-46b5-8c38-5137b2799c77")                      //TODO poner URL y consulta real
    fun getListSubContacts():Call<EmployeeListByBossID>
    //fun getListSubContacts(@Path("idsuperiorInmediato") idsuperiorInmediato: String):Call<ArrayList<Contacts>>


}