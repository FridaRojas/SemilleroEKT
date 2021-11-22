package com.example.agileus.webservices.dao

import com.example.agileus.config.InitialApplication
import com.example.agileus.models.Conversation
import com.example.agileus.models.Tasks
import retrofit2.Response

class TasksDao {

    suspend fun getTasks(): ArrayList<Tasks> {
        val callRespuesta = InitialApplication.webServiceGlobalTasks.getTasks()
        var ResponseDos: Response<ArrayList<Tasks>> = callRespuesta.execute()

        var lista = ArrayList<Tasks>()
        if (ResponseDos.isSuccessful) {
            lista = ResponseDos.body()!!
        }
        return lista
    }

    fun postTasks(t:Tasks){

    }







}