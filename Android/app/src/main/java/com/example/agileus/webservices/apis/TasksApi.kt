package com.example.agileus.webservices.apis


import com.example.agileus.models.DataTask
import com.example.agileus.models.TaskList
import com.example.agileus.models.Tasks
import retrofit2.Call
import retrofit2.http.*

interface TasksApi {

    //Obtener todas las Tareas
    @GET("tareas/obtenerTareas")
    fun getTasks(): Call<ArrayList<Tasks>>

    //Agregar Tarea nueva
    @POST("tareas/agregarTarea")
    fun insertarTarea(@Body t: Tasks): Call<Tasks>

    //Obtener lista por id, status
    @GET("tareas/obtenerTareasQueLeAsignaronPorIdYEstatus/{datos}")
    fun getTasksByStatus(@Path("datos") datos: String): Call<TaskList>?

    //Obtener una lista de personas en un grupo
    //@GET( )
    //fun getListaPersonasGrupo() : Call<ArrayList<PersonasGrupo>>


    @DELETE("tareas/cancelarTarea/{idTarea}")
    fun cancelarTarea(@Path("idTarea") idTarea: String): Call<DataTask>

}