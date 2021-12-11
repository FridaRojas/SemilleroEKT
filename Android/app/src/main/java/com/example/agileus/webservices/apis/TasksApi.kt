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

    //Obtener una lista de personas en un grupo
    @GET("{idsuperiorInmediato}")
    fun getListaPersonasGrupo(@Path("idsuperiorInmediato") idsuperiorInmediato: String): Call<PersonasGrupo>? // id lider

    //Agregar Tarea nueva
    @POST("tareas/agregarTarea")
    fun insertarTarea(@Body t: Tasks): Call<Tasks>

    //Obtener lista por id, status
    @GET("tareas/obtenerTareasQueLeAsignaronPorIdYEstatus/{idUsuario}/{estatus}")
    fun getTasksByStatus(
        @Path("idUsuario") idUsuario: String,
        @Path("estatus") estatus: String
    ): Call<TaskList>?

    //Cancelar tarea
    @DELETE("tareas/cancelarTarea/{idTarea}/{idUsuario}")
    fun cancelarTarea(
        @Path("idTarea") idTarea: String,
        @Path("idUsuario") idUsuario: String
    ): Call<DataTask>

    //Editar tarea
    @PUT("tareas/actualizarTarea/{idTarea}/{idUsuario}")
    fun editTask(
        @Body taskUpdate: TaskUpdate,
        @Path("idTarea") idTarea: String,
        @Path("idUsuario") idUsuario: String
    ): Call<DataTask>

    /*  @PUT("tareas/actualizarTarea/{idTarea}/{idUsuario}")
      fun editTaskPrueba(
          @Body taskUpdate: TaskUpdate,
          @Path("idTarea") idTarea: String,
          @Path("idUsuario") idUsuario: String
      ): Call<DataTask>*/


    //Actualizar Status
    @PUT("tareas/actulizarEstatus/{idTarea}/{estatus}")
    fun updateStatus(
        @Path("idTarea") idTarea: String,
        @Path("estatus") estatus: String
    ): Call<DataTask>

    //Obtener las tareas que asigno el usuario por id, status
    //@GET("tareas/obtenerTareasQueAsignoPorIdYEstatus/{datos}")
    @GET("tareas/obtenerTareasQueAsignoPorId/{idUsuario}")
    fun getTasksAssigned(@Path("idUsuario") idUsuario: String): Call<TaskList>?

}