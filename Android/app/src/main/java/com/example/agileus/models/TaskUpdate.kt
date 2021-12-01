package com.example.agileus.models

import com.google.gson.annotations.SerializedName

data class TaskUpdate(
    @SerializedName("titulo") var titulo: String,
    @SerializedName("descripcion") var descripcion: String,
    @SerializedName("fecha_ini") var fecha_ini: String,
    @SerializedName("fecha_fin") var fecha_fin: String,
    @SerializedName("prioridad") var prioridad: String,
    @SerializedName("estatus") var estatus: String,
    @SerializedName("observaciones") var observaciones: String,
)