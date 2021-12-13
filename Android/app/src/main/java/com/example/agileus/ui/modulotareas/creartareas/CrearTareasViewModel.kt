package com.example.agileus.ui.modulotareas.creartareas

import android.provider.Settings.Global.getString
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.R
import com.example.agileus.models.DataPersons
import com.example.agileus.models.Tasks
import com.example.agileus.ui.modulomensajeria.listaconversations.ListConversationViewModel
import com.example.agileus.ui.modulotareas.listenerstareas.DialogoConfirmOpStatusListener
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
                personasGrupoLista.value = listaGrupoPersonas
            }
        }
        catch (ex: Exception) {
            Log.e(ListConversationViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }

    fun  postTarea(t: Tasks,  listener: DialogoConfirmOpStatusListener){
        try {
            viewModelScope.launch {
                postTarea.postTasks(t, listener)
            }
        }
        catch (ex: Exception) {
            Log.e(ListConversationViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }

}