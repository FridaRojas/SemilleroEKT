package com.example.agileus.webservices.apis

import com.example.agileus.models.Buzon
import com.example.agileus.models.*
import retrofit2.Call
import retrofit2.http.*

interface MessageApi {

    @POST("mensajes/crearMensaje")
    fun mandarMensaje(@Body body:Message):Call<MessageResponse>

    @GET("mensajes/verConversacion/{id}")
    fun getConversationOnetoOne(@Path("id") idChat: String):Call<ResponseConversation>

    @GET("mensajes/listaContactos/{id}")
    fun getListContacts(@Path("id") idUser: String):Call<ResponseContacts>

    @GET("mensajes/listarConversaciones/{id}")
    fun getListChats(@Path("id") idUser: String):Call<ResponseChats>?

    @GET("mensajes/listaGrupos/{id}")
    fun getListGroups(@Path("id") idUser: String):Call<ResponseGroups>

    @PUT("mensajes/actualizarLeido")
    fun statusUpdate(@Body body:StatusRead):Call<MessageResponse>

}