package com.example.agileus.webservices.dao

import android.util.Log
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.Tasks
import retrofit2.Call
import retrofit2.Callback
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

        val callInserta = InitialApplication.webServiceGlobalTasks.insertarTarea(t)

        callInserta.enqueue(object : Callback<Tasks> {
            override fun onResponse(call: Call<Tasks>, response: Response<Tasks>) {

                if(response.isSuccessful){

                    val nuevaTarea: Tasks = response.body()!!
                    var mensaje= "Tarea creada por el emisor: ${nuevaTarea.nombreEmisor}"
                    mensaje+= " Titulo ${nuevaTarea.titulo}"
                    mensaje+= " Asignada a: ${nuevaTarea.nombreReceptor}"
                    mensaje+= " Descripcion:  ${nuevaTarea.descripcion}"
                    Log.d("Mensaje", mensaje.toString())

                }else{
                    Log.d("Mensaje", "No se creo la tarea ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Tasks>, t: Throwable) {}

        })

    }







}