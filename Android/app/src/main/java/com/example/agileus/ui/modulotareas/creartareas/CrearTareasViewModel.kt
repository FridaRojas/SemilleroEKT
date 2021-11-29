package com.example.agileus.ui.modulotareas.creartareas

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.models.Tasks
import com.example.agileus.ui.modulomensajeria.listaconversations.ListConversationViewModel
import com.example.agileus.webservices.dao.TasksDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CrearTareasViewModel: ViewModel() {

    var postTarea: TasksDao
    //var grupoLista = MutableLiveData<List<PersonasGrupo>>()

    init {
        postTarea = TasksDao()
    }

    fun  postTarea(t:Tasks){
        try {
            viewModelScope.launch {
                postTarea.postTasks(t)
            }
        }
        catch (ex: Exception) {
            Log.e(ListConversationViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }


    fun spinPersonasGrupo(id:String){
        //postTarea = MutableLiveData()
        try {
            viewModelScope.launch {
                val listaGrupoPersonas = withContext(Dispatchers.IO) {
                    //InitialApplication.webServiceGlobalTasks.getListaPersonasGrupo()
                }
                //postTarea.value = listaGrupoPersonas

            }

        }
        catch (ex: Exception) {
            Log.e(ListConversationViewModel::class.simpleName.toString(), ex.message.toString())
        }


    }

}