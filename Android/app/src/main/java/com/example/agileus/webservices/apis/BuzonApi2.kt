package com.example.agileus.webservices.apis


import com.example.agileus.adapters.BuzonAdapterResponse
import com.example.agileus.models.*
import com.example.agileus.utils.Constantes.id
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BuzonApi2 {



    @GET("broadCast/listaUsuarios/{id}")
    fun getList(@Path ("id") idUser:String):Call<ArrayList<ListaUsers>>

    @GET("broadCast/mostarMensajesdelBroadcast/{id}")
    fun getbuzon(@Path ("id") idUser:String):Call<ArrayList<BuzonResp>>

    @GET("mensajes/listarConversaciones/{id}")
    fun getmybuzon(@Path ("id") idUser:String):Call<ArrayList<BuzonComunicados>>



//
    @POST("broadCast/enviarMensaje")
    suspend fun pushpost(@Body Mensaje: MensajeBodyBroadcaster):Response<MensajeBodyBroadcaster>

    @POST("broadCast/crearMensajeBroadcast")
    suspend fun pushrequest(@Body Mensaje:MsgBodyUser):Response<MsgBodyUser>

//    @GET("mensajes/listaContactos/{id}")
  //  fun getListContacts(@Path("id") idUser: String):Call<ArrayList<Contacts>>

}