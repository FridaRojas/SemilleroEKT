package com.example.agileus.ui.modulotareas.listatareas

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.TasksAdapter
import com.example.agileus.models.Tasks
import com.example.agileus.ui.modulomensajeria.listacontactos.HomeViewModel
import com.example.agileus.ui.modulotareas.listenerstareas.DialogosTareasListener
import com.example.agileus.webservices.dao.TasksDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskViewModel() : ViewModel() {
    var adaptador = MutableLiveData<TasksAdapter>()
    lateinit var lista: TasksDao
    // lateinit var lista: ConversationDao

    //Cambiar el tipo de ArrayList a Tarea
    lateinit var listaConsumida: ArrayList<Tasks>

    //Lista de Estados Recycler

    init {
        lista = TasksDao()
    }

    fun devuelveLista(listener: DialogosTareasListener) {
        try {
            viewModelScope.launch {
                listaConsumida = withContext(Dispatchers.IO) {
                    lista.getTasks()
                }
                if (listaConsumida != null) {
                    if (listaConsumida.isNotEmpty()) {
                        adaptador.value =
                            TasksAdapter(listaConsumida as ArrayList<Tasks>, listener)
                    }
                }

            }


        } catch (ex: Exception) {
            Log.e(HomeViewModel::class.simpleName.toString(), ex.message.toString())
        }


    }
}