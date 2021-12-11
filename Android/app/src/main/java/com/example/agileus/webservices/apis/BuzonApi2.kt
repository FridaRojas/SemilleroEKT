package com.example.agileus.webservices.apis


import com.example.agileus.models.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface BuzonApi2 {
//------------------------------------------------------------------------------------------------------//
    @GET("broadCast/listaUsuarios/{id}")
    fun getList(@Path("id") idUser: String ,@Header ("tokenAuth") token: String):Call<Contacts1>

    @GET("broadCast/mostarMensajesdelBroadcast/{idUser}")
    fun getbuzon(@Path ("idUser") idUser:String,@Header ("tokenAuth") token: String):Call<BuzonResp1>

    @POST("broadCast/enviarMensaje") //
    suspend fun pushpost(@Body Mensaje: MensajeBodyBroadcaster,@Header ("tokenAuth") token: String):Response<MensajeBodyBroadcaster>

    @POST("broadCast/crearMensajeBroadcast/{idUser}")
    suspend fun pushrequest(@Path("idUser") idUser:String ,@Body Mensaje:MsgBodyUser, @Header ("tokenAuth") token: String):Response<MsgBodyUser>

    @GET("mensajes/verConversacion/{iduser}/{idchat}")
    fun getenviados(@Path ("iduser") iduser:String, @Path ("idchat") idchat:String, @Header ("tokenAuth") token: String):Call<BuzonComunicados> ///get enviados


    //------------------------------------------------------------------------------------------------------//
     @GET("mensajes/listarConversaciones/{iduser}")
       fun getmybuzon(@Path ("iduser") idUser:String,@Header ("tokenAuth") token: String):Call<Chats1> ///getbuzon enviados


    //  @GET("broadCast/mostrarMensajesporID/61ad370537670e5060dc060e/{id}"}
//  fun getmybuzon(@Path ("id") idUser:String):Call<Chats1> ///getbuzon enviados

  //  @GET("mensajes/listarConversaciones/{id}") ///broadCast/mostrarMensajesporID/61ad370537670e5060dc060e/61a83a48d036090b8e8db3bd
    //fun getmybuzon(@Path ("id") idUser:String):Call<Chats1> ///getbuzon recibidos




    //


  //  @GET("mensajes/listaContactos/{id}")
  //  fun getListContacts(@Path("id") idUser: String):Call<ArrayList<Contacts>>

}