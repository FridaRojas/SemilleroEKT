package com.example.agileus.webservices.apis

import com.example.agileus.ui.login.data.model.LoginResponse
import com.example.agileus.ui.login.data.model.Users
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    //Obtener usuario
    @POST("api/user/validate")
    fun iniciarSesionLogin(@Body usuario: Users) : Call<LoginResponse>


}