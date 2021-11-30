package com.example.agileus.ui.login.ui.login


import android.util.Log
import com.example.agileus.config.InitialApplication
import com.example.agileus.ui.login.data.model.Data
import com.example.agileus.ui.login.provider.LoginProviderListener
import com.example.agileus.ui.login.ui.login.services.GetUserDao
import retrofit2.Response

class LoginProvider : LoginProviderListener {
    suspend fun recupera(lista: ArrayList<Data>): ArrayList<Data> {
        val extra = GetUserDao(this)
        var callRespuesta = InitialApplication.userServiceLog.getRecuperaDatos()

        val listaUser = extra.obtenerLista(callRespuesta,lista)
        Log.d("Data","hola")
        return listaUser
    }


    override suspend fun obtenerLista(listaConsumida: ArrayList<Data>, respuesta2: Response<ArrayList<Data>>
    ): ArrayList<Data> {

        var lista = listaConsumida
        if (respuesta2.isSuccessful){
            lista = respuesta2.body()!!
        }
        return lista

    }


}