package com.example.agileus.webservices.apis


import com.example.agileus.models.LoginResponse
import com.example.agileus.models.Users
import retrofit2.Call
import retrofit2.http.*


interface LoginApi {

   //@POST("api/user/validate")
   // fun iniciarSesionLogin(@Body usuario:Users) : Call<LoginResponse>


    @POST("api/user/validate")
    fun iniciarSesionLogin(@Body usuario: Users) : Call<LoginResponse>


}

