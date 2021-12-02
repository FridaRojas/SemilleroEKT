package com.example.agileus.ui.login.data.service

import com.example.agileus.ui.login.data.model.Data
import com.example.agileus.ui.login.data.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface LoginApi {

   @GET("login%20v2.json?alt=media&token=49f226f5-eb9f-4213-97ee-7f726d4db02b")
    //fun userLogin  //se encarga de hacer el parseo a través de la llamada que le hacemos
    fun getUsers() : Call<ArrayList<User>>



/*    @GET("login%20v2.json?alt=media&token=49f226f5-eb9f-4213-97ee-7f726d4db02b")
    fun getUsers(
    @Query("status") status:String,
     @Query("msj") msj: String,
     @Query("data") data: String)
     : Call<ArrayList<User>>

 */


}

