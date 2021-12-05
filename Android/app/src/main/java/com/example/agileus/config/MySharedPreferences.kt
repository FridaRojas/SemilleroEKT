package com.example.agileus.config

import android.content.Context
import com.example.agileus.ui.login.data.model.Data

class MySharedPreferences(contexto: Context) {

    companion object{
        val TOKEN_KEY = "TOKEN_KEY"
        val SESSION_TOKEN = "TOKEN"

        //
        val id = "id"
        val nombre = "nombre"
        val nombreRol = "nombreRol"
        val token = "token"
        val idgrupo = "idgrupo"
        val idsuperiorInmediato = "idsuperiorInmediato"

    }

    val sharedPreferences = contexto.getSharedPreferences(SESSION_TOKEN, Context.MODE_PRIVATE)


    fun recuperarToken() : String{
        return sharedPreferences.getString(TOKEN_KEY, "")!!

    }

    fun recuperarUser(): String {
        return sharedPreferences.getString(id, " ")!!
        return sharedPreferences.getString(nombre, " ")!!
        return sharedPreferences.getString(nombreRol, " ")!!
        return sharedPreferences.getString(token, " ")!!
        return sharedPreferences.getString(idgrupo, " ")!!
        return sharedPreferences.getString(idsuperiorInmediato, " ")!!

    }

}