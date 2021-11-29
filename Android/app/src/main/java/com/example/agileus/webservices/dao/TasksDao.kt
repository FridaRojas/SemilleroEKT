package com.example.agileus.webservices.dao

import android.util.Log
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.DataTask
import com.example.agileus.models.TaskList
import com.example.agileus.models.Tasks
import com.example.agileus.ui.modulotareas.detalletareas.DetalleNivelAltoFragmentArgs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

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
    fun postTasks(t: Tasks) {
        val callInserta = InitialApplication.webServiceGlobalTasks.insertarTarea(t)
        callInserta.enqueue(object : Callback<Tasks> {
            override fun onResponse(call: Call<Tasks>, response: Response<Tasks>) {

                if (response.isSuccessful) {
                    val nuevaTarea: Tasks = response.body()!!
                    var mensaje =
                        "Tarea creada por el emisor:${nuevaTarea.nombreEmisor}" // Mensaje mostrado en el Log
                    mensaje += ", Titulo:${nuevaTarea.titulo}"
                    mensaje += ", Asignada a:${nuevaTarea.nombreReceptor}"
                    mensaje += ", Descripcion:${nuevaTarea.descripcion}"
                    mensaje += ", Fecha inicio:${nuevaTarea.fechaInicio}"
                    mensaje += ", Fecha fin:${nuevaTarea.fechaFin}"
                    Log.d("Mensaje", mensaje)
                } else {
                    Log.d("Mensaje", "No se creo la tarea ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Tasks>, t: Throwable) {}
        })
    }

    //Obtener tareas por status
    suspend fun getTasksByStatus(id: String, status: String): ArrayList<DataTask> {
        var listaTareas = ArrayList<DataTask>()
        lateinit var taskList: TaskList

        var datos = "$id&$status"
        val callRespuesta = InitialApplication.webServiceGlobalTasks.getTasksByStatus(datos)
        var response = callRespuesta?.execute()

        Log.d("tareas", listaTareas.toString())
        try {
            if (response != null) {
                if (response.isSuccessful) {
                    taskList = response.body()!!
                    if (taskList.data != null) {
                        listaTareas = taskList.data
                    } else {
                        listaTareas = emptyList<DataTask>() as ArrayList<DataTask>
                    }
                } else {
                    Log.e("error", "Errooor")
                }
            }
        } catch (e: Exception) {
            Log.e("error", e.toString())
        }

//        Log.d("tareas", listaTareas.toString())

        return listaTareas
    }


    fun cancelTask(t: DetalleNivelAltoFragmentArgs) {
        val callback = InitialApplication.webServiceGlobalTasks.cancelarTarea(t.tareas.idTarea)
        callback.enqueue(object : Callback<DataTask> {
            override fun onResponse(call: Call<DataTask>, response: Response<DataTask>) {
                if (response.isSuccessful) {
                    Log.d("Mensaje", "Eliminada")
                } else {
                    Log.d("Mensaje", "No se elimino tarea ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DataTask>, t: Throwable) {
                Log.d("Mensaje", "On Failure ${t.cause}")
            }
        })
    }


    fun editTask(t: DetalleNivelAltoFragmentArgs, idTarea: String) {
        Log.d("Mensaje", t.toString())
        val callback = InitialApplication.webServiceGlobalTasks.editTask(t, idTarea)
        callback.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Log.d("Mensaje", "${response.body()}")
                } else {
                    Log.d("Mensaje", "No se Edito tarea ${response.code()}")

                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("Mensaje", "On Failure: ${t.message}")
            }
        })
    }

     fun updateStatus(idTarea: String, estatus: String) {
        var url = idTarea + "&" + estatus
        url.trim()
        Log.d("url", url)
        val callback = InitialApplication.webServiceGlobalTasks.updateStatus(url)
//        val value: Response<String> = callback.execute()
//        Log.d("Mensaje", value.toString())
           callback.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Log.d("Mensaje", "Estatus Editado")
                } else {
                    Log.d("Mensaje", "No se Edito estatus ${response.code()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("Mensaje", "On Failure: ${t.message}")
            }

        })
    }
}