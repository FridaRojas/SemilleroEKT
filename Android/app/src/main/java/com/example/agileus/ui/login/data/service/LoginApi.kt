package com.example.agileus.ui.login.data.service


import com.example.agileus.ui.login.data.model.Data
import com.example.agileus.ui.login.data.model.LoginResponse
import com.example.agileus.ui.login.data.model.Users
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface LoginApi {

   @POST("api/user/validate")
    fun iniciarSesionLogin(@Body usuario:Users) : Call<LoginResponse>

    @FormUrlEncoded
    @POST("api/user/validate")
    fun userLogin(
        @Field("id") id:String,
        @Field("nombre") nombre:String,
        @Field("nombreRol") nombreRol:String,
        @Field("token") token:String,
        @Field("idgrupo") idgrupo:String,
        @Field("idsuperiorInmediato") idsuperiorInmediato:String

        ) : Call<Data>


}

