package com.example.agileus.webservices.apis


import com.example.agileus.models.Tasks
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TasksApi {

    //todo Falta editar el EndPoint para obtener las tareas
    @GET("tareas/obtenerTareas")
    fun getTasks(): Call<ArrayList<Tasks>>


    //todo Editar EndPoint para obtener personas de grupo
    //@GET( )
    //fun getListaPersonasGrupo() : Call<ArrayList<PersonasGrupo>>

    @POST("tareas/agregarTarea")
    fun insertarTarea(@Body t:Tasks): Call<Tasks>


}