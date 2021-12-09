package com.example.agileus.webservices.dao

import android.provider.ContactsContract
import android.util.Log
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.*
import com.example.agileus.ui.modulotareas.detalletareas.DetalleNivelAltoFragmentArgs
import com.example.agileus.ui.modulotareas.listenerstareas.DialogoConfirmacionListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class TasksDao {
    //Agregar nueva tarea
    fun postTasks(t: Tasks) {
        val callInserta = InitialApplication.webServiceGlobalTasks.insertarTarea(t)

        /*val Response = callInserta?.execute()
        try {
            if (Response != null) {
                if (Response.isSuccessful) {
                    val nuevaTarea: Tasks = Response.body()!!
                    var mensaje =
                        "Tarea creada por el emisor:${nuevaTarea.nombreEmisor}" // Mensaje mostrado en el Log
                    mensaje += ", Titulo:${nuevaTarea.titulo}"
                    mensaje += ", Asignada a:${nuevaTarea.nombreReceptor}"
                    mensaje += ", Numero de empleado:${nuevaTarea.idReceptor}"
                    mensaje += ", Descripcion:${nuevaTarea.descripcion}"
                    mensaje += ", Fecha inicio:${nuevaTarea.fechaInicio}"
                    mensaje += ", Fecha fin:${nuevaTarea.fechaFin}"
                    Log.d("Mensaje", mensaje)
                } else {
                    Log.d("Mensaje", "No se creo la tarea ${Response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e("error", e.toString())
        }*/

        callInserta.enqueue(object : Callback<Tasks> {
            override fun onResponse(call: Call<Tasks>, response: Response<Tasks>) {

                if (response.isSuccessful) {
                    val nuevaTarea: Tasks = response.body()!!
                    var mensaje =
                        "Tarea creada por el emisor: ${nuevaTarea.nombreEmisor}" // Mensaje mostrado en el Log
                    mensaje += ", Titulo:${nuevaTarea.titulo}"
                    mensaje += ", Asignada a:${nuevaTarea.nombreReceptor}"
                    mensaje += ", Numero de empleado:${nuevaTarea.idReceptor}"
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

        val callRespuesta = InitialApplication.webServiceGlobalTasks.getTasksByStatus(id, status)
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
                    listaTareas = emptyList<DataTask>() as ArrayList<DataTask>
                }
            } else {
                listaTareas = emptyList<DataTask>() as ArrayList<DataTask>
            }
        } catch (e: Exception) {
            Log.e("error", e.toString())
        }

//        Log.d("tareas", listaTareas.toString())

        return listaTareas
    }

    suspend fun getTasksAssigned(id: String): ArrayList<DataTask> {
        var listaTareasAsignadas = ArrayList<DataTask>()
        lateinit var taskList: TaskList

        val callRespuesta = InitialApplication.webServiceGlobalTasks.getTasksAssigned(id)
        var response = callRespuesta?.execute()

        try {
            if (response != null) {
                if (response.isSuccessful) {
                    taskList = response.body()!!
                    if (taskList.data != null) {
                        listaTareasAsignadas = taskList.data
                    } else {
                        listaTareasAsignadas = emptyList<DataTask>() as ArrayList<DataTask>
                    }
                } else {
                    listaTareasAsignadas = emptyList<DataTask>() as ArrayList<DataTask>
                }
            } else {
                listaTareasAsignadas = emptyList<DataTask>() as ArrayList<DataTask>
            }
        } catch (e: Exception) {
            Log.e("error", e.toString())
        }
        return listaTareasAsignadas
    }

    fun cancelTask(t: DetalleNivelAltoFragmentArgs) {
        val callback = InitialApplication.webServiceGlobalTasks.cancelarTarea(
            t.tareas.idTarea,
            t.tareas.idEmisor
        )
        callback.enqueue(object : Callback<DataTask> {
            override fun onResponse(call: Call<DataTask>, response: Response<DataTask>) {
                if (response.isSuccessful) {
                    Log.d("Mensaje", "Eliminada")
                } else {
                    Log.d("Mensaje", "No se elimino tarea ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DataTask>, t: Throwable) {
                Log.d("Mensaje", "On Failure ${t.message}")
            }
        })
    }


    fun editTask(taskUpdate: TaskUpdate, idTarea: String, idUsuario: String) {
        Log.d("Mensaje", taskUpdate.toString())
        Log.d("Mensaje", "id: ${idTarea}")
        val callback = InitialApplication.webServiceGlobalTasks.editTask(
            taskUpdate,
            idTarea,
            "618d9c26beec342d91d747d6"
        )
        callback.enqueue(object : Callback<DataTask> {
            override fun onResponse(
                call: Call<DataTask>,
                response: Response<DataTask>
            ) {
                try {
                    if (response.isSuccessful) {
                        Log.d(
                            "Mensaje",
                            "Tarea Editada ${response.message()}"
                        )
                    } else {
                        Log.d("Mensaje", "Tarea no Editada ${response.message()}")
                    }
                } catch (e: Exception) {
                    Log.d("Mensaje", e.message.toString())
                }
            }

            override fun onFailure(call: Call<DataTask>, t: Throwable) {
                Log.d("Mensaje", "On Failure: ${t.message}")
            }

        })
    }

    fun updateStatus(idTarea: String, estatus: String) {
//        var url = idTarea + "&" + estatus
//        url.trim()
//        Log.d("url", url)
        val callback = InitialApplication.webServiceGlobalTasks.updateStatus(idTarea, estatus)
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

    fun getPersonsGroup(idsuperiorInmediato: String): ArrayList<DataPersons> {
        lateinit var listaGrupoRecuperada: PersonasGrupo
        var listaPersonsDatos = ArrayList<DataPersons>()
        val callRespuestaPersonas =
            InitialApplication.webServiceGlobalTasksPersonas.getListaPersonasGrupo(
                idsuperiorInmediato
            )
        val Response = callRespuestaPersonas?.execute()
        try {
            if (Response != null) {
                if (Response.isSuccessful) {
                    listaGrupoRecuperada = Response.body()!!
                    Log.d("Mensaje", "listaGrupoRecuperada: ${listaGrupoRecuperada.status} ")
                    if (listaGrupoRecuperada.data != null) {
                        listaPersonsDatos = listaGrupoRecuperada.data
                    } else {
                        listaPersonsDatos = emptyList<DataPersons>() as ArrayList<DataPersons>
                    }

                } else {
                    Log.e("error", "Fallo la peticion ${Response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e("error", e.toString())
        }
        Log.d("Mensaje", "listaPersonsDatos size: ${listaPersonsDatos.size} ")
        return listaPersonsDatos
    }

}