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

    fun postTasks(t:Tasks){

        val callInserta = InitialApplication.webServiceGlobalTasks.insertarTarea(t)
        callInserta.enqueue(object : Callback<Tasks> {
            override fun onResponse(call: Call<Tasks>, response: Response<Tasks>) {

                if(response.isSuccessful){

                    val nuevaTarea: Tasks = response.body()!!
                    var mensaje= "Tarea creada por el emisor:${nuevaTarea.nombreEmisor}" // Mensaje mostrado en el Log
                    mensaje+= ", Titulo:${nuevaTarea.titulo}"
                    mensaje+= ", Asignada a:${nuevaTarea.nombreReceptor}"
                    mensaje+= ", Numero de empleado:${nuevaTarea.idReceptor}"
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

    fun getTasks(): ArrayList<Tasks> {
        val callRespuesta = InitialApplication.webServiceGlobalTasks.getTasks()
        val ResponseDos: Response<ArrayList<Tasks>> = callRespuesta.execute()
        var lista = ArrayList<Tasks>()
        if (ResponseDos.isSuccessful) {
            lista = ResponseDos.body()!!
        }
        return lista
    }
}