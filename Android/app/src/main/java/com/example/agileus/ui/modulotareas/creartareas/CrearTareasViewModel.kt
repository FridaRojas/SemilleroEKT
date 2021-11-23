package com.example.agileus.ui.modulotareas.creartareas

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.models.PersonasGrupo
import com.example.agileus.models.Tasks
import com.example.agileus.ui.modulomensajeria.listacontactos.HomeViewModel
import com.example.agileus.webservices.dao.TasksDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext




class CrearTareasViewModel: ViewModel() {

    //var adaptadorSpinPersonas = MutableLiveData<spinListaAsignarAdapter>()
    var postTarea: TasksDao
    var listaPersonas: TasksDao
    var personasGrupoLista = MutableLiveData<ArrayList<PersonasGrupo>>()

    init {
        postTarea = TasksDao()
        listaPersonas = TasksDao()
    }

    fun devuelvePersonasGrupo(id_grupo:String){
        personasGrupoLista = MutableLiveData()
        try {
            viewModelScope.launch {
                val listaGrupoPersonas = withContext(Dispatchers.IO) {
                    listaPersonas.getPersonsGroup(id_grupo)
                }
                //personasGrupoLista.value = listaGrupoPersonas
            }

        }
        catch (ex: Exception) {
            //Log.e(HomeViewModel::class.simpleName.toString(), ex.message.toString())
        }


    }

    fun  postTarea(t:Tasks){
        try {
            viewModelScope.launch {
                postTarea.postTasks(t)
            }
        }
        catch (ex: Exception) {
            Log.e(HomeViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }

}