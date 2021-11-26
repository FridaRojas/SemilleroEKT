package com.example.agileus.webservices.dao

import android.util.Log
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.DataTask
import com.example.agileus.models.TaskList
import com.example.agileus.models.DataPersons
import com.example.agileus.models.PersonasGrupo
import com.example.agileus.models.Tasks
import com.example.agileus.ui.modulotareas.detalletareas.DetalleNivelAltoFragmentArgs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class TasksDao {

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
                    mensaje+= ", Numero de empleado:${nuevaTarea.idReceptor}"
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

    fun editTask(t: DetalleNivelAltoFragmentArgs) {
        val callback = InitialApplication.webServiceGlobalTasks.editTask(t, t.tareas.idTarea)
        callback.enqueue(object : Callback<DataTask> {
            override fun onResponse(call: Call<DataTask>, response: Response<DataTask>) {
                if (response.isSuccessful) {
                    Log.d("Mensaje", "Tarea Editada")
                } else {
                    Log.d("Mensaje", "No se edito tarea ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DataTask>, t: Throwable) {
                Log.d("Mensaje", "On Failure ${t.cause}")
            }
        })
    }

    fun getPersonsGroup(idsuperiorInmediato:String): ArrayList<DataPersons>{
        lateinit var listaGrupoRecuperada : PersonasGrupo
        var listaPersonsDatos = ArrayList<DataPersons>()
        val callRespuestaPersonas = InitialApplication.webServiceGlobalTasksPersonas.getListaPersonasGrupo(idsuperiorInmediato)
        val Response = callRespuestaPersonas?.execute()
        try {
            if(Response != null) {
                if (Response.isSuccessful) {
                    listaGrupoRecuperada = Response.body()!!
                    Log.d("Mensaje", "listaGrupoRecuperada: ${listaGrupoRecuperada.status} ")
                    if(listaGrupoRecuperada.data != null){
                        listaPersonsDatos= listaGrupoRecuperada.data
                    }else{
                        listaPersonsDatos = emptyList<DataPersons>() as ArrayList<DataPersons>
                    }

                }else {
                    Log.e("error", "Fallo la peticion ${Response.code()}")
                }
            }
        }catch (e:Exception){
            Log.e("error", e.toString())
        }
        Log.d("Mensaje", "listaPersonsDatos: ${listaPersonsDatos.size} ")
        return listaPersonsDatos
    }
}