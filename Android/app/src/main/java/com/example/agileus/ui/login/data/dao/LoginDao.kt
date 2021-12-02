package com.example.agileus.ui.login.data.dao

import com.example.agileus.config.InitialApplication
import com.example.agileus.ui.login.data.model.Data
import com.example.agileus.ui.login.data.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


class LoginDao{
fun recuperarUser(lista: ArrayList<User>):ArrayList<User> {

    val callRespuesta = InitialApplication.LoginServiceGlobal.getUsers()
    var ResponseDos: Response<ArrayList<User>> = callRespuesta.execute()

    var listaconsumida=lista
        if (ResponseDos.isSuccessful){
            listaconsumida = ResponseDos.body()!!

        }
        return listaconsumida
    }
}

