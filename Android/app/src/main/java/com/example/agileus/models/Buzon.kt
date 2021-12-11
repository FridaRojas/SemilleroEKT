package com.example.agileus.models

import com.google.gson.annotations.SerializedName


data class Buzon(
    @SerializedName ("id") var id: String,
    @SerializedName ("Senderid") var Senderid: String,
    @SerializedName ("Receiverid") var Receiverid: String,
    @SerializedName ("Message") var Message:String,
    @SerializedName ("Asunto") var Asunto:String
)
//////////////////get buzon v2.1
data class BuzonResp(
    @SerializedName ("id") var id: String,
    @SerializedName ("idEmisor") var idemisor: String,
    @SerializedName ("descripcion") var descripcion: String,
    @SerializedName ("idReceptor") var idreceptor: String,
    @SerializedName ("nombreEmisor") var nombreEmisor:String
)


data class BuzonResp1(
    val data: List<DataBuzon>,
    val estatus: String,
    val mensaje: String
)

data class DataBuzon(

    @SerializedName ("id") var id: String,
    @SerializedName ("idEmisor") var idEmisor: String,
    @SerializedName ("descripcion") var descripcion: String,
    @SerializedName ("idReceptor") var idReceptor: String,
    @SerializedName ("nombreEmisor") var nombreEmisor:String,
    @SerializedName ("asunto")    val asunto: String,
    @SerializedName ("atendido") var atendido: Boolean,
    @SerializedName ("idBroadcast")val idBroadcast: Any,
    @SerializedName ("idGrupo")val idGrupo: String,
)
