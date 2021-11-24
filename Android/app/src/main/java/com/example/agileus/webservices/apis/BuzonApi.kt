package com.example.agileus.webservices.apis


import com.example.agileus.Models.Buzon
import retrofit2.Call
import retrofit2.http.GET

interface BuzonApi {

    @GET("listadeprueba.json?alt=media&token=f476d866-9b0e-4c3e-977c-a9d401c89fd1")
    fun getmensajesbuzon(): Call<ArrayList<Buzon>>

}