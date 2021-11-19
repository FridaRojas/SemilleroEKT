package com.example.agileus.ui.moduloreportes.reportes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReporteTareasViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Tareas reportes Fragment"
    }
    val text: LiveData<String> = _text
}