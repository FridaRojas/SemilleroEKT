package com.example.agileus.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class DataTask(
    @SerializedName("id_tarea") var idTarea: String,
    @SerializedName("id_grupo") var idGrupo: String,
    @SerializedName("id_emisor") var idEmisor: String,
    @SerializedName("nombre_emisor") var nombreEmisor: String,
    @SerializedName("id_receptor") var idReceptor: String,
    @SerializedName("nombre_receptor") var nombreReceptor: String,
    @SerializedName("fecha_ini") var fechaIni: Date,
    @SerializedName("fecha_iniR") var fechaIniR: Date,
    @SerializedName("fecha_fin") var fechaFin: Date,
    @SerializedName("fecha_finR") var fechaFinR: Date,
    @SerializedName("titulo") var titulo: String,
    @SerializedName("descripcion") var descripcion: String,
    @SerializedName("estatus") var estatus: String,
    @SerializedName("leido") var leido: Boolean,
    @SerializedName("fechaLeido") var fechaLeido: Date,
    @SerializedName("prioridad") var prioridad: String,
    @SerializedName("fecha_BD") var fechaBD: String,
    @SerializedName("observaciones") var observaciones: String,
    @SerializedName("archivo") var archivo: String,
    @SerializedName("token") var token: String,
    ) : Serializable