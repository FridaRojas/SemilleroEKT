package com.example.agileus.config

import android.content.Context

class MySharedPreferences(contexto: Context) {

    companion object{
        val TOKEN_KEY = "TOKEN_KEY"
        val SESSION_TOKEN = "TOKEN"
        val NIVEL_USER = "NIVEL_USER"
    }

    val sharedPreferences = contexto.getSharedPreferences(SESSION_TOKEN, Context.MODE_PRIVATE)

    fun recuperarToken() : String{
        return sharedPreferences.getString(TOKEN_KEY, "")!!
    }

    //todo guardar en sharedpreferences cuando el usuario inicie sesión
    fun guardarNivelUsuario(nivel:String) {
        with (sharedPreferences.edit()) {
            putString(NIVEL_USER, nivel)
            commit()
        }
    }

}