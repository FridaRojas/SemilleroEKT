package com.example.agileus.webservices.apis


import com.example.agileus.Models.Buzon
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface BuzonApi {

    @GET("api/Buzon/")
    fun getmensajesbuzon(): Call<ArrayList<Buzon>>

    @POST("api/Mensajes/")
    fun postmensajesbuzon(): Call<ArrayList<Buzon>>
}