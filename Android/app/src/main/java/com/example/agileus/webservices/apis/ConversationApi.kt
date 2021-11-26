package com.example.agileus.webservices.apis

import com.example.agileus.Models.Buzon
import com.example.agileus.models.Contacts
import com.example.agileus.models.Conversation
import com.example.agileus.models.Groups
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ConversationApi {


   //@GET("Conversacion618e8821c613329636a769ac_618e878ec613329636a769ab.json?alt=media&token=60d4f7b8-afe6-4aa6-bdfa-13126044b92c")
  // @GET("mensajes/verConversacion/618e8743c613329636a769aa_618b05c12d3d1d235de0ade0")
   @GET("mensajes/verConversacion/{id}")
   fun getConversationOnetoOne(@Path("id") idChat: String):Call<ArrayList<Conversation>>
   //fun getConversationOnetoOne(): Call<ArrayList<Conversation>>

   //@GET("ListaDeContactos.json?alt=media&token=1ad4bbfd-9db5-4842-85a1-377bffc10c9c")
   @GET("mensajes/listaContactos/{id}")
   fun getListContacts(@Path("id") idUser: String):Call<ArrayList<Contacts>>
    //fun getListContacts(): Call<ArrayList<Contacts>>

   @GET("ListaGrupos.json?alt=media&token=3dd4d650-e024-43b7-be48-b2250cd3fd82")
   fun getListGroups(): Call<ArrayList<Groups>>

    @GET("servicio.json?alt=media&token=e3076cf4-2c04-4609-ab51-8d3cbffdc6d8")
    fun getmensajesbuzon(): Call<ArrayList<Buzon>>
}