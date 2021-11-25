package com.example.agileus.ui.modulotareas.listatareas

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.TasksAdapter
import com.example.agileus.models.DataTask
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
        statusRecycler.value = "Pendiente"
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

    fun devolverListaPorStatus(){
        viewModelScope.launch {
                listaTask = withContext(Dispatchers.IO){
                    lista.getTasksByStatus("RECEPT1", statusRecycler.value.toString())
                }
                if (listaTask != null) {
                    adaptador.value = TasksAdapter(listaTask)
                }
            }
            if(listaTask.isNotEmpty()){
                adaptador.value?.update(listaTask)
            }
            Log.d("tarea", "${statusRecycler.value}")
    }
}