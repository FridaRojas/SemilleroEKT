package com.example.agileus.models

import com.google.gson.annotations.SerializedName

data class Tasks(
    @SerializedName("id_grupo") var idGrupo: String,
    @SerializedName("id_emisor") var idEmisor: String,
    @SerializedName("nombre_emisor") var nombreEmisor: String,
    @SerializedName("id_receptor") var idReceptor: String,
    @SerializedName("nombre_receptor") var nombreReceptor: String,
    @SerializedName("fecha_ini") var fechaInicio: String,
    @SerializedName("fecha_fin") var fechaFin: String,
    @SerializedName("titulo") var titulo: String,
    @SerializedName("id_mensaje") var idMensaje: String,
    @SerializedName("descripcion") var descripcion: String,
    @SerializedName("prioridad") var prioridad: String,
    @SerializedName("leido") var leido: Boolean,
    @SerializedName("createdDate") var createdDate: String
)