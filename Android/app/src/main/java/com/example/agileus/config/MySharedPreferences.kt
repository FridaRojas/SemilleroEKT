package com.example.agileus.config

import android.content.Context
import com.example.agileus.ui.login.data.model.Data

class MySharedPreferences(contexto: Context) {

    companion object{
        val TOKEN_KEY = "TOKEN_KEY"
        val SESSION_TOKEN = "TOKEN"

        //
        val id = "id"
        val correo = "correo"
        val password = "password"
        val nombre = "nombre"
        val nombreRol = "nombreRol"
        val token = "token"
        val idgrupo = "idgrupo"
        val idsuperiorInmediato = "idsuperiorInmediato"

    }

    val sharedPreferences = contexto.getSharedPreferences(SESSION_TOKEN, Context.MODE_PRIVATE)

    //
    val preferences = contexto.getSharedPreferences(id, Context.MODE_PRIVATE)


    fun recuperarToken() : String{
        return sharedPreferences.getString(TOKEN_KEY, "")!!

    }

    //LOGIN         // id:String, nombre:String , nombreRol:String, token:String, idgrupo:String, idsuperiorInmediato:String
    fun setUserPass(correo:String, password:String){
        //preferences.edit().putString("id", id).apply()
        preferences.edit().putString("correo", correo).apply()
        preferences.edit().putString("password", password).apply()
        //preferences.edit().putString("nombre", nombre).apply()
        //preferences.edit().putString("nombreRol", nombreRol).apply()
        //preferences.edit().putString("token", token).apply()
        //preferences.edit().putString("idgrupo", idgrupo).apply()
        //preferences.edit().putString("idsuperiorIntermedio", idsuperiorInmediato).apply()
    }
    fun getUserName():String?{
        return preferences.getString("correo", "4@gmail.com")
    }
    fun getPassword(): String?{
        return preferences.getString("password", "123")
    }
 /*   fun recuperarUser(): String {
        return sharedPreferences.getString(id, " ")!!
        return sharedPreferences.getString(nombre, " ")!!
        return sharedPreferences.getString(nombreRol, " ")!!
        return sharedPreferences.getString(token, " ")!!
        return sharedPreferences.getString(idgrupo, " ")!!
        return sharedPreferences.getString(idsuperiorInmediato, " ")!!
    }
  */

}