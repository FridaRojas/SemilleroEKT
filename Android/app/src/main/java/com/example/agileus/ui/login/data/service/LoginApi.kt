package com.example.agileus.ui.login.data.service


import com.example.agileus.ui.login.data.model.LoginResponse
import com.example.agileus.ui.login.data.model.Users
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface LoginApi {

   @POST("api/user/validaten")
    fun iniciarSesionLogin(@Body usuario:Users) : Call<LoginResponse>

}

