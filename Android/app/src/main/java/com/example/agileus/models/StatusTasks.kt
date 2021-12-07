package com.example.agileus.models

class StatusTasks(var status:String = "",
                  var isSelected:Boolean = false) {

    companion object{
        fun obtenerLista():ArrayList<StatusTasks>{
            var lista = ArrayList<StatusTasks>()
            lista.add(StatusTasks("Pendientes",true))
            lista.add(StatusTasks("Iniciadas",false))
            lista.add(StatusTasks("En Revisión",false))
            lista.add(StatusTasks("Terminadas",false))
            lista.add(StatusTasks("Asignadas",false))
            return lista
        }
    }
}