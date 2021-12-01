package com.example.agileus.ui.modulotareas.detalletareas

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.models.DataTask
import com.example.agileus.models.TaskUpdate
import com.example.agileus.webservices.dao.TasksDao
import kotlinx.coroutines.launch
import java.util.*

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

    fun editarTarea(taskUpdate: TaskUpdate, idTarea: String) {
        try {
            viewModelScope.launch {
                taskDao.editTask(taskUpdate, idTarea)
            }
        } catch (ex: Exception) {
            Log.e(DetalleNivelAltoViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }

    fun actualizarEstatus(dataTask: DataTask) {
        try {
            viewModelScope.launch {
                taskDao.updateStatus(dataTask.idTarea, dataTask.estatus)
                Log.d("Mensaje", "id: ${dataTask.idTarea}")
                Log.d("Mensaje", "estatus: ${dataTask.estatus}")
            }
        } catch (ex: Exception) {
            Log.e(DetalleNivelAltoViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }
}