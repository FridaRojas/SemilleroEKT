package com.example.agileus.webservices.apis


import com.example.agileus.models.DataTask
import com.example.agileus.models.TaskList
import com.example.agileus.models.PersonasGrupo
import com.example.agileus.models.Tasks
import com.example.agileus.ui.modulotareas.detalletareas.DetalleNivelAltoFragmentArgs
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TasksApi {

    //Obtener todas las Tareas
    @GET("tareas/obtenerTareas")
    fun getTasks(): Call<ArrayList<Tasks>>

    //Agregar Tarea nueva
    //todo Editar EndPoint para obtener personas de grupo
    //"http://10.97.0.165:3040/api/user/findAll"
    //@GET( "findAll")
    //fun getListaPersonasGrupo(): Call<PersonasGrupo>? // id lider


    //Obtener una lista de personas en un grupo
    //http://10.97.0.165:3040/api/user/findByBossId/618d9c26beec342d91d747d6
    @GET( "{idsuperiorInmediato}")
     fun getListaPersonasGrupo(@Path("idsuperiorInmediato") idsuperiorInmediato: String) : Call<PersonasGrupo>? // id lider

    @POST("tareas/agregarTarea")
    fun insertarTarea(@Body t: Tasks): Call<Tasks>

    //Obtener lista por id, status
    @GET("tareas/obtenerTareasQueLeAsignaronPorIdYEstatus/{datos}")
    fun getTasksByStatus(@Path("datos") datos: String): Call<TaskList>?

    @DELETE("tareas/cancelarTarea/{idTarea}")
    fun cancelarTarea(@Path("idTarea") idTarea: String): Call<DataTask>

    @PUT("tareas/actualizarTarea/{idTarea}")
    fun editTask(@Body t: DetalleNivelAltoFragmentArgs, @Path("idTarea") idTarea: String): Call<DataTask>


}