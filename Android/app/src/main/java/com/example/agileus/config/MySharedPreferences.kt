package com.example.agileus.config

import android.content.Context

class MySharedPreferences(contexto: Context) {

    companion object{
        val TOKEN_KEY = "TOKEN_KEY"
        val SESSION_TOKEN = "TOKEN"

        val ID_USER_KEY = "ID_USER_KEY"
        val FIRST_NAME_KEY = "FIRST_NAME_KEY"
        val NOMBRE_ROL_KEY = "NOMBRE_ROL_KEY"
        val EMAIL_KEY = "EMAIL_KEY"
        //val PASSWORD_KEY ="PASSWORD_KEY"
        val ID_GRUPO_KEY = "ID_GRUPO"
        val ID_SUPERIOR_INTERMEDIO_KEY = "ID_SUPERIO_INTERMEDIO_KEY"

    }

    val sharedPreferences = contexto.getSharedPreferences(SESSION_TOKEN, Context.MODE_PRIVATE)

    fun recuperarToken() : String{
        return sharedPreferences.getString(TOKEN_KEY, "")!!
        sharedPreferences.edit()

    }

    fun guardarDatos(id:String, nombre:String, nombreRol:String, correo:String, idgrupo:String, idsuperiorInmediato:String, sesion:Boolean) {
        with(sharedPreferences.edit()){
            putString(ID_USER_KEY, id)
            putString(FIRST_NAME_KEY, nombre)
            putString(NOMBRE_ROL_KEY, nombreRol)
            putString(EMAIL_KEY, correo)
            //putString(TOKEN_KEY, token)
            putString(ID_GRUPO_KEY, idgrupo)
            putString(ID_SUPERIOR_INTERMEDIO_KEY, idsuperiorInmediato)
            putBoolean(TOKEN_KEY,sesion)
            commit()
        }
    }

    //sustituir x la de arriba (fun)
    fun recuperarId():String{
        return sharedPreferences.getString(TOKEN_KEY, "")!!
    }

    fun recuperarCorreo():String{
        return sharedPreferences.getString(EMAIL_KEY, "")!!
    }

    fun validaSesionIniciada(): Boolean{
        return sharedPreferences.getBoolean(TOKEN_KEY, false)
    }

    fun cerrarSesion(){
        sharedPreferences.edit().clear()
    }

}