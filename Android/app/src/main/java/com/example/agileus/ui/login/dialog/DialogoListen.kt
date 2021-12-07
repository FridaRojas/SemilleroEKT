package com.example.agileus.ui.login.dialog



interface DialogoListen {
    //fun editRecuperarPassword(correo:String, password:String)
    //Funciones que se van a disparar desde otro lado
    fun siDisparar(motivo:String)
    fun noDisparar(motivo: String)

}