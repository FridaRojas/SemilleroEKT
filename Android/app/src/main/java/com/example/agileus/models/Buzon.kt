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


data class BuzonComunicados(
    @SerializedName("id") val id: String,
    @SerializedName("conversacionVisible") val conversacionVisible: Boolean,
    @SerializedName("texto") val texto: String,
    @SerializedName("nombreConversacionReceptor") val nombreConversacionReceptor: String,
    @SerializedName("idreceptor") var idreceptor: String,
    @SerializedName("idemisor") var idemisor: String,
)
