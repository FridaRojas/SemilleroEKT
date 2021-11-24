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

        if (response != null) {
            if (response.isSuccessful) {
                taskList = response.body()!!
                listaTareas = taskList.data
            } else {
                Log.e("error", "Errooor")
            }
        }
        Log.d("tareas", listaTareas.toString())

        return listaTareas
    }

    fun cancelTask(dataTask: DetalleNivelAltoFragmentArgs) {

        val callBack = InitialApplication.webServiceGlobalTasks.cancelarTarea(dataTask.tareas.idTarea)
        callBack.enqueue(object : Callback<DataTask> {
            override fun onResponse(call: Call<DataTask>, response: Response<DataTask>) {
//                val tareaCancelada: DataTask = response.body()!!
//                Log.d("Mensaje", tareaCancelada.estatus)
            }

            override fun onFailure(call: Call<DataTask>, t: Throwable) {
                Log.d("Mensaje", "On Failure:  ${t.cause}")
            }

        })

    }


}