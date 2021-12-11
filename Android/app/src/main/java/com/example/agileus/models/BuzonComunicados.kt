package com.example.agileus.models

import com.google.gson.annotations.SerializedName

data class BuzonComunicados (

    @SerializedName ("data")   val data: List<Datas>,
    @SerializedName ("msj")    val msj: String,
    @SerializedName ("status") val status: String
    )

    data class Datas(
        @SerializedName ("conversacionVisible")  val conversacionVisible: Boolean,
        @SerializedName ("fechaCreacion") var fechaCreacion: String,
        @SerializedName ("fechaEnviado")        val fechaEnviado: String,
        @SerializedName ("fechaLeido")        val fechaLeido: String,
        @SerializedName ("id")        val id: String,
        @SerializedName ("idconversacion")        val idconversacion: String,
        @SerializedName ("idemisor")        var idemisor: String,
        @SerializedName ("idreceptor")        var idreceptor: String,
        @SerializedName ("nombreConversacionReceptor")        val nombreConversacionReceptor: String,
        @SerializedName ("rutaDocumento")        val rutaDocumento: Any,
        @SerializedName ("statusCreado")        val statusCreado: Boolean,
        @SerializedName ("statusEnviado")        val statusEnviado: Boolean,
        @SerializedName ("statusLeido")        val statusLeido: Boolean,
        @SerializedName ("statusRutaDocumento")        val statusRutaDocumento: Boolean,
        @SerializedName ("texto")        val texto: String,
        @SerializedName ("visible")        val visible: Boolean
    )
