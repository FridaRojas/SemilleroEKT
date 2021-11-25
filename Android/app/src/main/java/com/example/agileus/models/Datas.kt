package com.example.agileus.models

import com.google.gson.annotations.SerializedName

data class Datas(
    @SerializedName("correo")       val correo: String,
    @SerializedName("curp")         val curp: String,
    @SerializedName("fechaInicio")  val fechaInicio: String,
    @SerializedName("fechaTermino") val fechaTermino: String,
    @SerializedName("id")           val id: String,
    @SerializedName("idgrupo")      val idgrupo: String,
    @SerializedName("idsuperiorInmediato") val idsuperiorInmediato: String,
    @SerializedName("nombre")       val nombre: String,
    @SerializedName("nombreRol")    val nombreRol: String,
    @SerializedName("numeroEmpleado") val numeroEmpleado: String,
    @SerializedName("opcionales")   val opcionales: List<Any>,
    @SerializedName("password")     val password: String,
    @SerializedName("rfc")          val rfc: String,
    @SerializedName("statusActivo") val statusActivo: String,
    @SerializedName("telefono")     val telefono: String,
    @SerializedName("token")        val token: String
)