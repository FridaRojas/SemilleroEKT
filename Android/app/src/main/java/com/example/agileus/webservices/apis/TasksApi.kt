package com.example.agileus.webservices.apis


import com.example.agileus.models.PersonasGrupos
import com.example.agileus.models.Tasks
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TasksApi {

    //todo Falta editar el EndPoint para obtener las tareas
    @GET("tareas/obtenerTareas")
    fun getTasks(): Call<ArrayList<Tasks>>


    //todo Editar EndPoint para obtener personas de grupo
    @GET( "    Url    ")
    //fun getListaPersonasGrupo(@Query("id_grupo") id_grupo: String) : Call<ArrayList<PersonasGrupo>>
    fun getListaPersonasGrupo(@Path("id_emisor") id_emisor: String) : Call<ArrayList<PersonasGrupos>> // id lider


    @POST("tareas/agregarTarea")
    fun insertarTarea(@Body t:Tasks): Call<Tasks>

}