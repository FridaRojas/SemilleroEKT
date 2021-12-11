package com.example.agileus.config

import android.content.Context
import android.content.SharedPreferences
import com.example.agileus.models.Data

class MySharedPreferences(contexto: Context) {

    companion object {
        val SESSION_TOKEN = "TOKEN"
        val NIVEL_USER = "NIVEL_USER"

        val SESSION_KEY = "SESSION_KEY"
        val NIVEL_HIJOS = "NIVEL_HIJOS"
        //Login
        val ID_SESSION = "ID_USER"
        val CORREO_SESSION = "CORREO_SESSION"
        val FECHA_INICIO = "FECHA_INICIO"
        val FECHA_TERMINO = "FECHA_TERMINO"
        val NUMERO_EMPLEADO = "NUMERO_EMPLEADO_SESSION"
        val NOMBRE_SESSION = "NOMBRE_SESSION"
        val NOMBRE_ROL_SESSION = "NOMBRE_ROL_SESSION"
        val TOKEN_KEY = "TOKEN_KEY" /*Token notificaciones*/
        val TELEFONO_SESSION = "TELEFONO_SESION"
        val STATUS_ACTIVO = "STATUS_ACTIVO"
        val CURP_SESSION = "CURP_SESSION"
        val RFC_SESSION = "RFC_SESSION"
        val TOKEN_AUTH_KEY = "TOKEN_AUTH_KEY"   /*Token Seguridad*/
        val ID_GRUPO_SESSION = "ID_GRUPO_SESSION"
        val ID_SUPERIOR_INMEDIATO = "ID_SUPERIOR_INMEDIATO"
    }

    val sharedPreferences = contexto.getSharedPreferences(SESSION_TOKEN, Context.MODE_PRIVATE)
    private lateinit var editor: SharedPreferences.Editor

    //Inicio de Sesión
    fun guardarDatosInicioSesion(
        id : String,  correo: String, numEmpleado:String,
        nombre:String, nombreRol:String,
        telefono:String, curp:String, rfc:String, tokenAuth:String,
        idGrupo:String, idSuperior:String, sessionKey:Boolean
    ){
        with(sharedPreferences.edit()) {
            putString(ID_SESSION, id)
            putString(CORREO_SESSION, correo)
            putString(NUMERO_EMPLEADO, numEmpleado)
            putString(NOMBRE_SESSION, nombre)
            putString(NOMBRE_ROL_SESSION, nombreRol)
            putString(TELEFONO_SESSION, telefono)
            putString(CURP_SESSION, curp)
            putString(RFC_SESSION, rfc)
            putString(TOKEN_AUTH_KEY, tokenAuth)
            putString(ID_GRUPO_SESSION, idGrupo)
            putString(  ID_SUPERIOR_INMEDIATO, idSuperior)
            putBoolean(SESSION_KEY, sessionKey)
            commit()
        }
    }

    fun recuperarNombreSesion() : String{
        return sharedPreferences.getString(NOMBRE_SESSION, "")!!
    }
    fun recuperarTokenAuth() : String{
        return sharedPreferences.getString(TOKEN_AUTH_KEY, "")!!
    }

    fun recuperarIdSesion() : String{
        return sharedPreferences.getString(ID_SESSION, "")!!
    }

    fun recuperarIdGrupoSesion() : String{
        return sharedPreferences.getString(ID_GRUPO_SESSION, "")!!
    }

    fun recuperarIdSuperiorInmediato() : String{
        return sharedPreferences.getString(ID_SUPERIOR_INMEDIATO, "")!!
    }

    fun validaSesionIniciada(): Boolean{
        return sharedPreferences.getBoolean(SESSION_KEY, false)
    }

    fun cerrarSesion(){
        sharedPreferences.edit().clear().commit()
    }

    //Token Notificaciones
    fun recuperarToken(): String {
        return sharedPreferences.getString(TOKEN_KEY, "")!!
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