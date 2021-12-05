package com.example.agileus.webservices.apis


import com.example.agileus.models.*
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
    @GET("{idsuperiorInmediato}")
    fun getListaPersonasGrupo(@Path("idsuperiorInmediato") idsuperiorInmediato: String): Call<PersonasGrupo>? // id lider


    @POST("tareas/agregarTarea")
    //fun insertarTarea(@Body t: Tasks): Call<Tasks>
    fun insertarTarea(@Body t: Tasks)

    //Obtener lista por id, status
    @GET("tareas/obtenerTareasQueLeAsignaronPorIdYEstatus/{datos}")
    fun getTasksByStatus(@Path("datos") datos: String): Call<TaskList>?

    //Cancelar tarea
    @DELETE("tareas/cancelarTarea/{idTarea}")
    fun cancelarTarea(@Path("idTarea") idTarea: String): Call<DataTask>

    //Editar tarea
    @PUT("tareas/actualizarTarea/{idTarea}")
    fun editTask(@Body taskUpdate: TaskUpdate, @Path("idTarea") idTarea: String)
            : Call<TaskList2>

    //Actualizar Status
    @PUT("tareas/actulizarEstatus/{param}")
    fun updateStatus(@Path("param") param: String): Call<String>

    //Obtener las tareas que asigno el usuario por id, status
    //@GET("tareas/obtenerTareasQueAsignoPorIdYEstatus/{datos}")
    @GET("tareas/obtenerTareasQueAsignoPorId/{datos}")
    fun getTasksAssigned(@Path("datos") datos: String): Call<TaskList>?

}