package com.example.agileus.models

import com.google.gson.annotations.SerializedName

data class Tasks(

    // agregar prioridad en minusculas y agregar status minusculas pendiente
    @SerializedName("id_grupo") var idGrupo: String,
    @SerializedName("id_emisor") var idEmisor: String,
    @SerializedName("nombre_emisor") var nombreEmisor: String,
    @SerializedName("id_receptor") var idReceptor: String,
    @SerializedName("nombre_receptor") var nombreReceptor: String,
    @SerializedName("fecha_ini") var fechaInicio: String,
    @SerializedName("fecha_fin") var fechaFin: String,
    @SerializedName("titulo") var titulo: String,
    //@SerializedName("id_mensaje") var idMensaje: String,
    @SerializedName("descripcion") var descripcion: String,
    @SerializedName("prioridad") var prioridad: String,
    @SerializedName("estatus") var estatus: String,
    @SerializedName("leido") var leido: Boolean,
    @SerializedName("createdDate") var createdDate: String
)

/*data class PersonasGrupos(
    @SerializedName("data")     val `data`: List<Datas>,
    @SerializedName("msj")      val msj: String,
    @SerializedName("status")   val status: String
)*/

/*data class Datas(
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
)*/