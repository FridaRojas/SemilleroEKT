package com.example.agileus.webservices.apis

import com.example.agileus.models.Tasks
import retrofit2.Call
import retrofit2.http.GET

interface TasksApi {

    //todo Falta editar el EndPoint para obtener las tareas
    @GET("tareas/getTareas")
    fun getTasks(): Call<ArrayList<Tasks>>

}