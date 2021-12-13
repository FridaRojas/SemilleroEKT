package com.example.agileus.config

import android.content.Context
import android.content.SharedPreferences

class TokenSharedPreferences (contexto: Context) {

    companion object {
        val SESSION_TOKEN = "TOKEN"
        val TOKEN_KEY = "TOKEN_KEY" /*Token notificaciones*/
    }

    val sharedPreferences = contexto.getSharedPreferences(TokenSharedPreferences.SESSION_TOKEN, Context.MODE_PRIVATE)
    private lateinit var editor: SharedPreferences.Editor


    //Token Notificaciones
    fun recuperarToken(): String {
        return sharedPreferences.getString(TOKEN_KEY, "")!!
    }

}