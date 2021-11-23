package com.example.agileus.webservices.apis

import com.example.agileus.models.Message
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MessageApi {

    @POST("mensajes/crearMensaje")
    fun mandarMensaje(@Body body:Message):Call<Message>


}