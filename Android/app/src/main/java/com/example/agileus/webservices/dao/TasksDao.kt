package com.example.agileus.webservices.dao

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.PersonasGrupos
import com.example.agileus.models.Tasks
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.modulotareas.creartareas.FormularioCrearTareasFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.coroutineContext

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


    fun getPersonsGroup(id_grupo:String): ArrayList<PersonasGrupos>{
        val callRespuesta = InitialApplication.webServiceGlobalTasks.getListaPersonasGrupo(id_grupo)
        val Response: Response<ArrayList<PersonasGrupos>> = callRespuesta.execute()

        var listaPersons = ArrayList<PersonasGrupos>()
        if (Response.isSuccessful) {
            listaPersons = Response.body()!!
        }
        return listaPersons
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







}