package com.example.agileus.models

import com.example.agileus.R

class Status(var status:String = "",
              var isSelected:Boolean = false) {

    companion object{
        fun obtenerLista():ArrayList<Status>{
            var lista = ArrayList<Status>()
            lista.add(Status("Pendientes",true))
            lista.add(Status("Iniciadas",false))
            lista.add(Status("En Revisión",false))
            lista.add(Status("Terminadas",false))
            lista.add(Status("Asignadas",false))
            return lista
        }
    }
}