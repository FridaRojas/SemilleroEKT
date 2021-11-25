package com.example.agileus.ui.modulotareas.creartareas

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.models.DataPersons
import com.example.agileus.models.Tasks
import com.example.agileus.ui.modulomensajeria.listacontactos.HomeViewModel
import com.example.agileus.webservices.dao.TasksDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CrearTareasViewModel: ViewModel() {

    var postTarea       : TasksDao
    var personasGetDao   : TasksDao
    var personasGrupoLista = MutableLiveData<ArrayList<DataPersons>>()

    init {
        postTarea = TasksDao()
        personasGetDao  = TasksDao()
    }

     fun devuelvePersonasGrupo(idsuperiorInmediato : String){
        personasGrupoLista = MutableLiveData()
        try {
            viewModelScope.launch {
                val listaGrupoPersonas = withContext(Dispatchers.IO) {
                    personasGetDao .getPersonsGroup(idsuperiorInmediato)
                }
                Log.d("Mensaje", "listaGrupoPersonas CrearTareasViewModel: ${listaGrupoPersonas.size} ")
                personasGrupoLista.value = listaGrupoPersonas
            }
        }
        catch (ex: Exception) {
            Log.e(HomeViewModel::class.simpleName.toString(), ex.message.toString())
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