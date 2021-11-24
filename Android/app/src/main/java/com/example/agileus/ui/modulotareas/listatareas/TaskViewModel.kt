package com.example.agileus.ui.modulotareas.listatareas

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.TasksAdapter
import com.example.agileus.models.DataTask
import com.example.agileus.models.Tasks
import com.example.agileus.ui.modulotareas.listenerstareas.TaskDialogListener
import com.example.agileus.ui.modulotareas.listenerstareas.TaskListListener
import com.example.agileus.webservices.dao.TasksDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskViewModel() : ViewModel() {
    var adaptador = MutableLiveData<TasksAdapter>()
    lateinit var lista: TasksDao
    // lateinit var lista: ConversationDao

    //Lista de Estados Recycler
    lateinit var listaConsumida : ArrayList<Tasks>
    lateinit var listaTask:ArrayList<DataTask>

    //Cambiar el tipo de ArrayList a Tarea
    init {
        lista = TasksDao()
    }

    /*
    fun devuelveLista() {
        try {
            viewModelScope.launch {
                listaConsumida = withContext(Dispatchers.IO) {
                    lista.getTasks()
                }
                if (listaConsumida != null) {
                    if (listaConsumida.isNotEmpty()) {
                        adaptador.value =
                            TasksAdapter(listaConsumida)
                    }
                }
            }
        } catch (ex: Exception) {
            Log.e(HomeViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }*/

    fun devolverListaPorStatus(listener: TaskListListener){
            viewModelScope.launch {
                listaTask = withContext(Dispatchers.IO){
                    lista.getTasksByStatus("RECEPT1", "Pendiente")
                }
                if (listaTask != null) {
                    if (listaTask.isNotEmpty()) {
                        adaptador.value =
                            TasksAdapter(listaTask, listener)
                    }
                }
            }

    }
}