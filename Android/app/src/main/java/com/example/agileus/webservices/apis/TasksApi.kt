package com.example.agileus.webservices.apis


import com.example.agileus.models.PersonasGrupo
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
    //"http://10.97.0.165:3040/api/user/findAll"
    @GET( "user/findAll")
    fun getListaPersonasGrupo(): Call<PersonasGrupo>? // id lider


    //todo Editar EndPoint para obtener personas de grupo
    //http://10.97.0.165:3040/api/user/findByBossId/618d9c26beec342d91d747d6
    //@GET( "{idsuperiorInmediato}")
    //fun getListaPersonasGrupo(@Path("idsuperiorInmediato") idsuperiorInmediato: String) : Call<PersonasGrupo>? // id lider

    @POST("tareas/agregarTarea")
    fun insertarTarea(@Body t:Tasks): Call<Tasks>

}