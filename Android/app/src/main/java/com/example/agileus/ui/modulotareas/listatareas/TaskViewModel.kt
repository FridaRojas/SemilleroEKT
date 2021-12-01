package com.example.agileus.ui.modulotareas.listatareas

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.TasksAdapter
import com.example.agileus.models.DataTask
import com.example.agileus.ui.modulotareas.listenerstareas.TaskListListener
import com.example.agileus.webservices.dao.TasksDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskViewModel() : ViewModel() {


    var statusRecycler = MutableLiveData<String>()
    var adaptador = MutableLiveData<TasksAdapter>()

    var lista: TasksDao = TasksDao()
    var listaTask = ArrayList<DataTask>()
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

        viewModelScope.launch {
                listaTask = withContext(Dispatchers.IO){
                    lista.getTasksByStatus("4758399642", statusRecycler.value.toString())
                    if(statusRecycler.value.toString() == "asignada"){
                        //Listas asignadas
                            //idEmisor
                        lista.getTasksByStatus("618b05c12d3d1d235de0ade0", statusRecycler.value.toString())
                    }else{
                        //idReceptor
                        lista.getTasksAssigned("618b05c12d3d1d235de0ade0", statusRecycler.value.toString())
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