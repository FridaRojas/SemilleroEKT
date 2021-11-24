package com.example.agileus.webservices.dao

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.DataTask
import com.example.agileus.models.TaskList
import com.example.agileus.models.Tasks
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.modulotareas.creartareas.FormularioCrearTareasFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.coroutineContext

class TasksDao {

    //Obtener todas las tareas
    suspend fun getTasks(): ArrayList<Tasks> {
        val callRespuesta = InitialApplication.webServiceGlobalTasks.getTasks()
        var ResponseDos: Response<ArrayList<Tasks>> = callRespuesta.execute()

        var lista = ArrayList<Tasks>()
        if (ResponseDos.isSuccessful) {
            lista = ResponseDos.body()!!
        }
        return lista
    }

    //Agregar nueva tarea
    fun postTasks(t:Tasks){
        val callInserta = InitialApplication.webServiceGlobalTasks.insertarTarea(t)
        callInserta.enqueue(object : Callback<Tasks> {
            override fun onResponse(call: Call<Tasks>, response: Response<Tasks>) {

                if(response.isSuccessful){
                    val nuevaTarea: Tasks = response.body()!!
                    var mensaje= "Tarea creada por el emisor:${nuevaTarea.nombreEmisor}" // Mensaje mostrado en el Log
                    mensaje+= ", Titulo:${nuevaTarea.titulo}"
                    mensaje+= ", Asignada a:${nuevaTarea.nombreReceptor}"
                    mensaje+= ", Descripcion:${nuevaTarea.descripcion}"
                    mensaje+= ", Fecha inicio:${nuevaTarea.fechaInicio}"
                    mensaje+= ", Fecha fin:${nuevaTarea.fechaFin}"
                    Log.d("Mensaje", mensaje)
                }else{
                    Log.d("Mensaje", "No se creo la tarea ${response.code()}")
                }
            }
            override fun onFailure(call: Call<Tasks>, t: Throwable) {}
        })
    }

    //Obtener tareas por status
    fun getTasksByStatus(id:String, status:String) :ArrayList<Tasks> {
        var listaTareas = ArrayList<Tasks>()

        var datos = "$id&$status"
        val callRespuesta = InitialApplication.webServiceGlobalTasks.getTasksByStatus(datos)
        callRespuesta?.enqueue(object : Callback<ArrayList<Tasks>> {
            override fun onResponse(
                call: Call<ArrayList<Tasks>>,
                response: Response<ArrayList<Tasks>>
            ) {
                if (response.isSuccessful) {
                    listaTareas = response.body()!!
                }
            }

            override fun onFailure(call: Call<ArrayList<Tasks>>, t: Throwable) {
                Log.e("error", "${t.message}")
            }
        })

        return listaTareas
    }


}