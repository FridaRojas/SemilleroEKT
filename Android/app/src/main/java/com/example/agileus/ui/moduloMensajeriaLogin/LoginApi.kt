package com.example.agileus.ui.moduloMensajeriaLogin

import com.example.agileus.models.User
import retrofit2.Call
import retrofit2.http.GET

interface LoginApi {
    @GET("login%20v2.json?alt=media&token=49f226f5-eb9f-4213-97ee-7f726d4db02b")
    //fun userLogin  //se encarga de hacer el parseo a través de la llamada que le hacemos
    fun getUsers() : Call<ArrayList<User>>


}
