package com.example.agileus.config

import android.content.Context

class MySharedPreferences(contexto: Context) {

    companion object{
        val TOKEN_KEY = "TOKEN_KEY"
        val SESSION_TOKEN = "TOKEN"

    }

    val sharedPreferences = contexto.getSharedPreferences(SESSION_TOKEN, Context.MODE_PRIVATE)

    fun recuperarToken() : String{
        return sharedPreferences.getString(TOKEN_KEY, "")!!

    }

}