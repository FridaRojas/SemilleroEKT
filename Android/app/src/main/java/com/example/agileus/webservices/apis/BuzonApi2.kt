package com.example.agileus.webservices.apis

import com.example.agileus.models.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface BuzonApi2 {

//  @Headers("2aa0dbb48b80e5c3f3fa8aef7d26d72ed330cfbfdac5c6862ebd6c281402bb52")
    @GET("broadCast/listaUsuarios/{id}")
    fun getList(@Path ("id") idUser:String):Call<ArrayList<Contacts>>

  //  @Headers("2aa0dbb48b80e5c3f3fa8aef7d26d72ed330cfbfdac5c6862ebd6c281402bb52")
    @GET("broadCast/mostarMensajesdelBroadcast/{id}")
    fun getbuzon(@Path ("id") idUser:String):Call<ArrayList<BuzonResp>>

    @GET("mensajes/listarConversaciones/{id}")
    fun getmybuzon(@Path ("id") idUser:String):Call<ArrayList<Chats>> ///getbuzon recibidos

    @GET("mensajes/verConversacion/{id}")
    fun getenviados(@Path ("id") idUser:String):Call<ArrayList<BuzonComunicados>> ///get enviados

//
    @POST("broadCast/enviarMensaje")
    suspend fun pushpost(@Body Mensaje: MensajeBodyBroadcaster):Response<MensajeBodyBroadcaster>

    @POST("broadCast/crearMensajeBroadcast")
    suspend fun pushrequest(@Body Mensaje:MsgBodyUser):Response<MsgBodyUser>

  //  @GET("mensajes/listaContactos/{id}")
  //  fun getListContacts(@Path("id") idUser: String):Call<ArrayList<Contacts>>

}