package com.example.agileus.webservices.apis

import com.example.agileus.Models.Buzon
import com.example.agileus.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MessageApi {

    @POST("mensajes/crearMensaje")
    fun mandarMensaje(@Body body:Message):Call<MessageResponse>

    @GET("mensajes/verConversacion/{id}")
    fun getConversationOnetoOne(@Path("id") idChat: String):Call<ArrayList<Conversation>>

    @GET("mensajes/listaContactos/{id}")
    fun getListContacts(@Path("id") idUser: String):Call<ArrayList<Contacts>>

    @GET("ListaGrupos.json?alt=media&token=3dd4d650-e024-43b7-be48-b2250cd3fd82")
    fun getListGroups(): Call<ArrayList<Groups>>

    @GET("servicio.json?alt=media&token=e3076cf4-2c04-4609-ab51-8d3cbffdc6d8")
    fun getmensajesbuzon(): Call<ArrayList<Buzon>>

}