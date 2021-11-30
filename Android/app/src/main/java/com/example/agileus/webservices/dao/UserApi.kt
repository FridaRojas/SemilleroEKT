package com.example.agileus.webservices.dao

import com.example.agileus.config.InitialApplication
import com.example.agileus.ui.login.data.model.Data
import com.example.agileus.ui.login.data.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi{
    @GET("login%20v2.json?alt=media&token=49f226f5-eb9f-4213-97ee-7f726d4db02b")
    //fun userLogin
    //se encarga de hacer el parseo a través de la llamada que le hacemos
    fun getRecuperarDatosLogin() : Call<ArrayList<Data>>

}