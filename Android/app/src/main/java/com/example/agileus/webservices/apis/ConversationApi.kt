package com.example.agileus.webservices.apis

import com.example.agileus.Models.Buzon
import com.example.agileus.models.Conversation
import retrofit2.Call
import retrofit2.http.GET

interface ConversationApi {

  @GET("mensajes/verConversacion/618d9c26beec342d91d747d6_618e8743c613329636a769aa")
   // @GET("servicio.json?alt=media&token=e3076cf4-2c04-4609-ab51-8d3cbffdc6d8")
    fun getConversationOnetoOne(): Call<ArrayList<Conversation>>



  @GET("mensajes/listaContactos/618d9c26beec342d91d747d6")
  // @GET("servicio.json?alt=media&token=e3076cf4-2c04-4609-ab51-8d3cbffdc6d8")
  fun getListContacts(): Call<ArrayList<Contacts>>



    @GET("servicio.json?alt=media&token=e3076cf4-2c04-4609-ab51-8d3cbffdc6d8")
    fun getmensajesbuzon(): Call<ArrayList<Buzon>>
}