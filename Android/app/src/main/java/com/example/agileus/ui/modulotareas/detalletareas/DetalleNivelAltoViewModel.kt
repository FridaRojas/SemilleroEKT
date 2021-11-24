package com.example.agileus.ui.modulotareas.detalletareas

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
}