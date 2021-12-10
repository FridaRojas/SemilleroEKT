package com.example.agileus.models

class StatusTasks(var status : String = "",
                  var isSelected:Boolean = false) {

    companion object{
        var lista = ArrayList<StatusTasks>()

        fun obtenerListaNivelAlto() : ArrayList<StatusTasks>{
            lista.clear()
            lista.add(StatusTasks("Asignadas",true))
            return lista
        }

        fun obtenerListaNivelMedio() : ArrayList<StatusTasks>{
            lista.clear()
            lista.add(StatusTasks("Pendientes",true))
            lista.add(StatusTasks("Iniciadas",false))
            lista.add(StatusTasks("En Revisión",false))
            lista.add(StatusTasks("Terminadas",false))
            lista.add(StatusTasks("Asignadas",false))
            return lista
        }

        fun obtenerListaNivelBajo() : ArrayList<StatusTasks>{
            lista.clear()
            lista.add(StatusTasks("Pendientes",true))
            lista.add(StatusTasks("Iniciadas",false))
            lista.add(StatusTasks("En Revisión",false))
            lista.add(StatusTasks("Terminadas",false))
            return lista
        }
    }
}