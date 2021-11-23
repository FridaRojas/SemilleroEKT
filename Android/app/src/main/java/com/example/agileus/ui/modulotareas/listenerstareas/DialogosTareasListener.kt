package com.example.agileus.ui.modulotareas.listenerstareas

import com.example.agileus.models.Tasks

interface DialogosTareasListener {

    fun onDateInicioSelected(anio: Int, mes: Int, dia: Int)

    fun onDateFinSelected(anio: Int, mes: Int, dia: Int)

    fun abreDialogoNivelBajo()

}