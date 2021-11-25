package com.example.agileus.models

import com.google.gson.annotations.SerializedName

data class DatosMensajes(

    @SerializedName("id")val id: String,
    @SerializedName("texto")val texto: String,
    @SerializedName("visible")val visible:Boolean,
    @SerializedName("nombreConversacionReceptor")val nombreConversacionReceptor:String,
    @SerializedName("fechaCreacion")val fechaCreacion:String,
    @SerializedName("statusCreado")val statusCreado:Boolean,
    @SerializedName("fechaEnviado")val fechaEnviado:String,
    @SerializedName("statusEnviado")val statusEnviado:Boolean,
    @SerializedName("fechaLeido")val fechaLeido:String,
    @SerializedName("statusLeido")val statusLeido:Boolean,
    @SerializedName("idemisor")val idemisor: String,
    @SerializedName("idreceptor")val idreceptor: String,
    @SerializedName("idconversacion")val idconversacion: String
)