package com.example.agileus.ui.modulotareas.detalletareas

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.models.Tasks
import com.example.agileus.webservices.dao.TasksDao
import kotlinx.coroutines.launch

class DetalleNivelAltoViewModel : ViewModel() {
    var taskDao: TasksDao

    init {
        taskDao = TasksDao()
    }

    fun cancelarTarea(dataTask: DetalleNivelAltoFragmentArgs) {
        try {
            viewModelScope.launch {
                taskDao.cancelTask(dataTask)
            }
        } catch (ex: Exception) {
            Log.e(DetalleNivelAltoViewModel::class.simpleName.toString(), ex.message.toString())
        }

    }

    fun editarTarea(dataTask: DetalleNivelAltoFragmentArgs) {
        /*    var tarea = Tasks(
                dataTask.tareas.idGrupo,
                dataTask.tareas.idEmisor,
                dataTask.tareas.nombreEmisor,
                dataTask.tareas.idReceptor,
                dataTask.tareas.nombreReceptor,
                "",
                "",
                dataTask.tareas.titulo,
                "0",
                dataTask.tareas.descripcion,
                dataTask.tareas.prioridad,
                false,
                "0"
            )*/
        try {
            viewModelScope.launch {
                taskDao.editTask(dataTask, dataTask.tareas.idTarea)
                Log.d("Mensaje", "id: ${dataTask.tareas.idTarea}")
            }
        } catch (ex: Exception) {
            Log.e(DetalleNivelAltoViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }
}