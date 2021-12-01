package com.example.agileus.ui.modulotareas.listatareas

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.TasksAdapter
import com.example.agileus.config.InitialApplication.Companion.preferenciasGlobal
import com.example.agileus.models.DataTask
import com.example.agileus.ui.modulotareas.listenerstareas.TaskListListener
import com.example.agileus.webservices.dao.TasksDao
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskViewModel() : ViewModel() {

    companion object{
        var status = "pendiente"
    }

    var statusRecycler = MutableLiveData<String>()
    var adaptador = MutableLiveData<TasksAdapter>()

    var lista: TasksDao = TasksDao()
    var listaTask = ArrayList<DataTask>()
    var listaTaskAssigned = ArrayList<DataTask>()

    //lateinit var listaConsumida : ArrayList<Tasks>

    init {
        statusRecycler.value = "pendiente"
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
        status = statusRecycler.value.toString()
        viewModelScope.launch {
                listaTask = withContext(Dispatchers.IO){
                    if(statusRecycler.value.toString() == "asignada"){
                        //id Emisor
                        lista.getTasksAssigned("618d9c26beec342d91d747d6")
                    }else{
                        //id Receptor -> id Receptor
                        lista.getTasksByStatus("4758399642", statusRecycler.value.toString())
                    }
                }
                if (listaTask != null) {
                    adaptador.value = TasksAdapter(listaTask,listener)
                }
            }
            if(listaTask.isNotEmpty()){
                adaptador.value?.update(listaTask)
            }
            Log.d("tarea", "${statusRecycler.value}")
    }

}