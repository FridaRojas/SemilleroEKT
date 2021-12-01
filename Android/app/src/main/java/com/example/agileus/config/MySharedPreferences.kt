package com.example.agileus.config

import android.content.Context

class MySharedPreferences(contexto: Context) {

    companion object{
        val TOKEN_KEY = "TOKEN_KEY"
    }

    var SESSION_TOKEN = "TOKEN"
    val sharedPreferences = contexto.getSharedPreferences(SESSION_TOKEN, Context.MODE_PRIVATE)


    fun guardarToken(token:String){
        with (sharedPreferences.edit()) {
            putString(TOKEN_KEY, token)
            commit()
        }
    }



}