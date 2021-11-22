package com.example.agileus.webservices.apis

import com.example.agileus.models.Conversation
import com.example.agileus.models.Tasks
import com.example.agileus.webservices.dao.TasksDao
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TasksApi {

    //todo Falta editar el EndPoint para obtener las tareas
    @GET("servicio.json?alt=media&token=e3076cf4-2c04-4609-ab51-8d3cbffdc6d8")
    fun getTasks(): Call<ArrayList<Tasks>>


    //todo Editar EndPoint para obtener personas de grupo
    //@GET( )
    //fun getListaPersonasGrupo() : Call<ArrayList<PersonasGrupo>>

    @POST("addTarea")
    fun insertarTarea(@Body t:Tasks): Call<Tasks>


}