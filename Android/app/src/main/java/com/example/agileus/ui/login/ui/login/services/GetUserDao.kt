package com.example.agileus.ui.login.ui.login.services

import android.util.Log
import com.example.agileus.config.InitialApplication
import com.example.agileus.ui.login.data.model.Data

import com.example.agileus.ui.login.provider.LoginProviderListener
import retrofit2.Call
import retrofit2.Response

class GetUserDao(val listener: LoginProviderListener) {

    suspend fun obtenerLista(callRespuesta:Call<ArrayList<Data>>, lista:ArrayList<Data>): ArrayList<Data>{

        //var callRespuesta = InitialApplication.userServiceLog.getRecuperaDatos()
        var respuesta : Response<ArrayList<Data>> = callRespuesta.execute()
        var listaUser = listener.obtenerLista(lista,respuesta)

        Log.d("Paso","hola")

        return listaUser
    }
}


