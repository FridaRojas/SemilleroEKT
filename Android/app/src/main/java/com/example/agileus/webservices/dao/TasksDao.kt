package com.example.agileus.webservices.dao


import android.util.Log
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.DataPersons
import com.example.agileus.models.PersonasGrupo
import com.example.agileus.models.Tasks
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TasksDao {

    fun getPersonsGroup(): ArrayList<DataPersons>{
        lateinit var listaPersons : PersonasGrupo
        var listaPersonsRecuperada = ArrayList<DataPersons>()

        val callRespuestaPersonas = InitialApplication.webServiceGlobalTasksPersonas.getListaPersonasGrupo()
        if (callRespuestaPersonas != null) {
            callRespuestaPersonas.enqueue(object: Callback<PersonasGrupo>{
                override fun onResponse(call: Call<PersonasGrupo>, response: Response<PersonasGrupo>) {
                    if (response.isSuccessful) {
                        if(response.body()!=null) {
                            listaPersons = response.body()!!
                            listaPersonsRecuperada = listaPersons.data

                            listaPersonsRecuperada.forEach {
                                val id      =it.numeroEmpleado
                                val title   = it.nombre
                                Log.d("Mensaje", "El mensaje es $id . El title es $title")
                            }
                        }
                    }else{
                        Log.d("Mensaje", "Fallo la peticion ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<PersonasGrupo>, t: Throwable) {
                    Log.d("Mensaje", "onFailure  $t")
                }

            })
        }
        return listaPersonsRecuperada
    }

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

    suspend fun getTasks(): ArrayList<Tasks> {
        val callRespuesta = InitialApplication.webServiceGlobalTasks.getTasks()
        var ResponseDos: Response<ArrayList<Tasks>> = callRespuesta.execute()
        var lista = ArrayList<Tasks>()
        if (ResponseDos.isSuccessful) {
            lista = ResponseDos.body()!!
        }
        return lista
    }
}