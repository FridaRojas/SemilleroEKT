package com.example.agileus.config

import android.content.Context
import android.content.SharedPreferences
import com.example.agileus.ui.login.data.model.Data

class MySharedPreferences(contexto: Context) {

    companion object {
        val TOKEN_KEY = "TOKEN_KEY"
        val SESSION_TOKEN = "TOKEN"
        val NIVEL_USER = "NIVEL_USER"

        //
        //val BASE_DATOS_KEY = "BD_PREFERENCIAS_DOS"
        //val ID_KEY = "ID_KEY"
        val CORREO_KEY = "CORREO_KEY"
        val PASSWORD_KEY = "PASWORD_KEY"


    }

    val sharedPreferences = contexto.getSharedPreferences(SESSION_TOKEN, Context.MODE_PRIVATE)
    private lateinit var editor: SharedPreferences.Editor


    fun recuperarToken(): String {
        return sharedPreferences.getString(TOKEN_KEY, "")!!
    }

    //LOGIN WITH SHARED
    fun iniciarSesion(correo: String, password: String, sesion: Boolean) {
        with(sharedPreferences.edit()) {
            //putString(ID_KEY, id)
            putString(CORREO_KEY, correo)
            putString(PASSWORD_KEY, password)
            putBoolean(TOKEN_KEY, sesion)
            editor.commit()
        }
    }

    fun recuperaNombre(): String {
        return sharedPreferences.getString(CORREO_KEY, " ")!!
    }

    fun recuperaPassword(): String {
        return sharedPreferences.getString(PASSWORD_KEY, " ")!!
    }

    fun validaSesionIniciada(): Boolean {
        return sharedPreferences.getBoolean(SESSION_TOKEN, false)
    fun validaSesionIniciada():Boolean{
        return sharedPreferences.getBoolean(SESSION_TOKEN,false)
    }

    fun cerrarSesion() {
        //BORRA TODO LO QUE ESTA GUARDADO
        sharedPreferences.edit().clear().apply()
    }

    //todo guardar en sharedpreferences cuando el usuario inicie sesión
    fun guardarNivelUsuario(nivel: String) {
        with(sharedPreferences.edit()) {
            putString(NIVEL_USER, nivel)
            commit()
        }
    }

    fun recuperarNivelUsuario() : String{
        return sharedPreferences.getString(NIVEL_USER, "")!!
    }


}