package com.example.agileus.models

import com.google.gson.annotations.SerializedName


data class Buzon(
    @SerializedName ("id") var id: String,
    @SerializedName ("Senderid") var Senderid: String,
    @SerializedName ("Receiverid") var Receiverid: String,
    @SerializedName ("Message") var Message:String,
    @SerializedName ("Asunto") var Asunto:String
)


data class BuzonResp(
    @SerializedName ("id") var id: String,
    @SerializedName ("idEmisor") var Senderid: String,
    @SerializedName ("descripcion") var Receiverid: String,
    @SerializedName ("idReceptor") var Message:String,
    @SerializedName ("nombreEmisor") var Asunto:String
)


data class BuzonComunicados(
    @SerializedName("id") val id: String,
    @SerializedName("conversacionVisible") val conversacionVisible: Boolean,
    @SerializedName("texto") val texto: String,
    @SerializedName("nombreConversacionReceptor") val nombreConversacionReceptor: String,
    @SerializedName("idreceptor") val idreceptor: String,
    @SerializedName("idemisor") val idemisor: String,
)
