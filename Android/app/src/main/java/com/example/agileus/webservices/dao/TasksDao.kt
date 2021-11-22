package com.example.agileus.webservices.dao


import android.content.Context
import android.util.Log
import android.widget.Toast
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

        val callInserta = InitialApplication.webServiceGlobalTasks2.insertarTarea(t)

        callInserta.enqueue(object : Callback<Tasks> {
            override fun onResponse(call: Call<Tasks>, response: Response<Tasks>) {

                if(response.isSuccessful){

                    val nuevaTarea: Tasks = response.body()!!

                    var mensaje= "Tarea creada por el emisor: ${nuevaTarea.nombreEmisor}"

                    mensaje+=  "Title ${nuevaTarea.titulo}"
                    mensaje+= " Asignada a: ${nuevaTarea.nombreReceptor}"
                    mensaje+= " Descripcion:  ${nuevaTarea.descripcion}"

                  //  Toast.makeText(, mensaje, Toast.LENGTH_SHORT).show()
                    Log.d("Mensaje", mensaje.toString())

                }else{
                    Log.d("Mensaje", "No se creo la tarea ${response.code()}")

                    //Toast.makeText(, "No se creo la tarea  ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Tasks>, t: Throwable) {

            }
        })

    }







}