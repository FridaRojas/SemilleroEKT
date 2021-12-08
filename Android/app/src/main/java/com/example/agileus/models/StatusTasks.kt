package com.example.agileus.models

class StatusTasks(var status : String = "",
                  var isSelected:Boolean = false) {

    companion object{
        var lista = ArrayList<StatusTasks>()

        fun obtenerLista() : ArrayList<StatusTasks>{
            return lista
        }
    }
}