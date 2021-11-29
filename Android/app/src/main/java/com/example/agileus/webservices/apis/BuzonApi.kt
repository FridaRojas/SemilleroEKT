package com.example.agileus.webservices.apis


import com.example.agileus.Models.Buzon
import com.example.agileus.models.Tasks
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BuzonApi {

    @GET("servicio.json?alt=media&token=e3076cf4-2c04-4609-ab51-8d3cbffdc6d8")
    fun getmensajesbuzon(): Call<ArrayList<Buzon>>

}