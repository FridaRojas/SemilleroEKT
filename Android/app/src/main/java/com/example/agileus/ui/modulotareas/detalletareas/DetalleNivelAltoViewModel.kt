package com.example.agileus.ui.modulotareas.detalletareas

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.models.DataTask
import com.example.agileus.models.TaskUpdate
import com.example.agileus.ui.modulotareas.dialogostareas.DialogoActualizarTarea
import com.example.agileus.ui.modulotareas.listenerstareas.dialogoConfirmarListener
import com.example.agileus.webservices.dao.TasksDao
import kotlinx.coroutines.launch
import java.util.*

class DetalleNivelAltoViewModel : ViewModel() {
    var taskDao: TasksDao

    init {
        taskDao = TasksDao()
    }

    fun cancelarTarea(dataTask: DetalleNivelAltoFragmentArgs, listener: dialogoConfirmarListener) {
        try {
            viewModelScope.launch {
                taskDao.cancelTask(dataTask, listener)
            }
        } catch (ex: Exception) {
            Log.e(DetalleNivelAltoViewModel::class.simpleName.toString(), ex.message.toString())
        }

    }

    fun editarTarea(
        dataTask: TaskUpdate,
        idTarea: String,
        idUsuario: String,
        listener: dialogoConfirmarListener
    ) {
        try {
            viewModelScope.launch {
                taskDao.editTask(dataTask, idTarea, idUsuario, listener)
            }
        } catch (ex: Exception) {
            Log.e(DetalleNivelAltoViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }

    fun actualizarEstatus(dataTask: DataTask, listener: dialogoConfirmarListener) {
        try {
            viewModelScope.launch {
                taskDao.updateStatus(dataTask.idTarea, dataTask.estatus, listener)
                Log.d("Mensaje", "id: ${dataTask.idTarea}")
                Log.d("Mensaje", "estatus: ${dataTask.estatus}")
            }
        } catch (ex: Exception) {
            Log.e(DetalleNivelAltoViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }

    fun actualizarEstatus(dataTask: DetalleNivelAltoFragmentArgs,listener: dialogoConfirmarListener) {
        try {
            viewModelScope.launch {
                taskDao.updateStatus(dataTask.tareas.idTarea, dataTask.tareas.estatus,listener)
                Log.d("Mensaje", "id: ${dataTask.tareas.idTarea}")
                Log.d("Mensaje", "estatus: ${dataTask.tareas.estatus}")
            }
        } catch (ex: Exception) {
            Log.e(DetalleNivelAltoViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }
}