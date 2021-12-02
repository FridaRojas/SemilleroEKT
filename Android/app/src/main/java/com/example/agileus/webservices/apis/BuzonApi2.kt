package com.example.agileus.webservices.apis


import com.example.agileus.models.Buzon
import com.example.agileus.models.ListaUsers
import com.example.agileus.models.MensajeBodyBroadcaster
import com.example.agileus.models.MsgBodyUser
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

    @POST("broadCast/enviarMensaje")
    suspend fun pushpost(@Body Mensaje: MensajeBodyBroadcaster):Response<MensajeBodyBroadcaster>

    @POST("broadCast/crearMensajeBroadcast")
    suspend fun pushrequest(@Body Mensaje:MsgBodyUser):Response<MsgBodyUser>



//    @GET("mensajes/listaContactos/{id}")
  //  fun getListContacts(@Path("id") idUser: String):Call<ArrayList<Contacts>>

}