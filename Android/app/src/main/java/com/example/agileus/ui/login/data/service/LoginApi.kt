package com.example.agileus.ui.login.data.service


import com.example.agileus.models.PersonasGrupo
import com.example.agileus.models.TaskList
import com.example.agileus.models.UserBossResponse
import com.example.agileus.ui.login.data.model.LoginResponse
import com.example.agileus.ui.login.data.model.Users
import com.example.agileus.ui.login.data.model.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.*


interface LoginApi {

   //@POST("api/user/validate")
   // fun iniciarSesionLogin(@Body usuario:Users) : Call<LoginResponse>


    @POST("api/user/validate")
    fun iniciarSesionLogin(@Body usuario:Users) : Call<LoginResponse>


    @GET("user/findByBossId/{id}")
    fun getUsersByBoss(@Path("id") datos: String): Call<UserBossResponse>?


}

