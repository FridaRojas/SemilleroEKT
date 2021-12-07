package com.example.agileus.models

import com.google.gson.annotations.SerializedName
import retrofit2.Call


//////////usuario Broadcast a normal  //api               10.97.6.83:3040/api/broadCast/enviarMensaje

class MensajeBodyBroadcaster(
    @SerializedName ("fechaCreacion") val fechaCreacion: String,
    @SerializedName ("idEmisor")val idEmisor: String,
    @SerializedName ("idReceptor")val idReceptor: String,
    @SerializedName ("texto") val texto: String
)

//////////usuario   normal a broacast                    // 10.97.6.83:3040/api/broadCast/crearMensajeBroadcast


class MsgBodyUser(
    @SerializedName ("asunto")    val asunto: String,
    @SerializedName ("descripcion")    val descripcion: String,
    @SerializedName ("idEmisor")    val idEmisor: String
)


