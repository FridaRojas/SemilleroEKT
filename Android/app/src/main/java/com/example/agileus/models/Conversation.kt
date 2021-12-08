package com.example.agileus.models

import com.google.gson.annotations.SerializedName

data class Conversation(
   @SerializedName("id") val id: String,
   @SerializedName("texto") val texto: String,
   @SerializedName("visible") var visible: Boolean,
   @SerializedName("rutaDocumento") var rutaDocumento: String,
   @SerializedName("statusRutaDocumento") val statusRutaDocumento:Boolean,
   @SerializedName("nombreConversacionReceptor") val nombreConversacionReceptor:String,
   @SerializedName("fechaCreacion") val fechaCreacion:String,
   @SerializedName("statusCreado") val statusCreado:Boolean,
   @SerializedName("fechaEnviado") val fechaEnviado:String,
   @SerializedName("statusEnviado") val statusEnviado:Boolean,
   @SerializedName("fechaLeido") val fechaLeido:String,
   @SerializedName("statusLeido") val statusLeido:Boolean,
   @SerializedName("idemisor") val idemisor:String,
   @SerializedName("idconversacion") val idconversacion:String,
   @SerializedName("idreceptor") val idreceptor:String,

)
class conversartionListByID(
   @SerializedName("estatus") val estatus: String,
   @SerializedName("mensaje") val mensaje: String,
   @SerializedName("data") val data: ArrayList<Conversation>
)