package com.example.agileus.webservices.dao

import com.example.agileus.config.InitialApplication
import com.example.agileus.models.User
import retrofit2.Response

class LoginDao {

    fun recuperarUser(lista: ArrayList<User>):ArrayList<User> {

        val callRespuesta = InitialApplication.LoginServiceGlobal.getUsers()
        var ResponseDos: Response<ArrayList<User>> = callRespuesta.execute()
///
        var listaconsumida=lista
        if (ResponseDos.isSuccessful){
            listaconsumida = ResponseDos.body()!!
        }
        return listaconsumida
    }
}



