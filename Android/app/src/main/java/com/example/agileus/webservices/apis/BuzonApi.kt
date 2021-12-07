package com.example.agileus.webservices.apis


import com.example.agileus.models.Buzon
import com.example.agileus.models.ListaUsers
import com.example.agileus.utils.Constantes.id
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BuzonApi {

    @GET("api/Buzon")
    fun getmensajesbuzon():Call<ArrayList<Buzon>>

    @POST("api/Mensajes")
    suspend fun pushpost(@Body buzon:Buzon):Response<Buzon>

 }