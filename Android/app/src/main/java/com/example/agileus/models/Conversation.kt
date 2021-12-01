package com.example.agileus.models

import com.google.gson.annotations.SerializedName

data class Conversation(
   @SerializedName("id") val id: String,
   @SerializedName("conversacionVisible") val conversacionVisible: Boolean,
   @SerializedName("texto") val texto: String,
   @SerializedName("visible") val visible: Boolean,
   @SerializedName("rutaDocumento") val rutaDocumento: String,
   @SerializedName("statusRutaDocumento") val statusRutaDocumento: Boolean,
   @SerializedName("nombreConversacionReceptor") val nombreConversacionReceptor: String,
   @SerializedName("fechaCreacion") val fechaCreacion: String,
   @SerializedName("statusCreado") val statusCreado: Boolean,
   @SerializedName("fechaEnviado") val fechaEnviado: String,
   @SerializedName("statusEnviado") val statusEnviado: Boolean,
   @SerializedName("fechaLeido") val fechaLeido: String,
   @SerializedName("statusLeido") val statusLeido: Boolean,
   @SerializedName("idreceptor") val idreceptor: String,
   @SerializedName("idemisor") val idemisor: String,
   @SerializedName("idconversacion") val idconversacion: String,
)