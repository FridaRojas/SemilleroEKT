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
    @GET("tareas/obtenerTareasQueLeAsignaronPorIdYEstatus/{datos}")
    fun getTasksByStatus(@Path("datos") datos: String): Call<TaskList>?

    //Cancelar tarea
    @DELETE("tareas/cancelarTarea/{idTarea}")
    fun cancelarTarea(@Path("idTarea") idTarea: String): Call<DataTask>

    //Editar tarea
    @PUT("tareas/actualizarTarea/{idTarea}")
    fun editTask(@Body taskUpdate: TaskUpdate, @Path("idTarea") idTarea: String)
            : Call<DataTask>

   /* @PUT("tareas/actualizarTarea/{idTarea}/{idUsuario}")
    fun editTaskPrueba(
        @Body taskUpdate: TaskUpdate, @Path("idTarea") idTarea: String,
        @Path("idUsuario") idUsuario: String
    )
            : Call<DataTask>*/


    //Actualizar Status
    @PUT("tareas/actulizarEstatus/{param}")
    fun updateStatus(@Path("param") param: String): Call<String>

    //Obtener las tareas que asigno el usuario por id, status
    //@GET("tareas/obtenerTareasQueAsignoPorIdYEstatus/{datos}")
    @GET("tareas/obtenerTareasQueAsignoPorId/{datos}")
    fun getTasksAssigned(@Path("datos") datos: String): Call<TaskList>?

}