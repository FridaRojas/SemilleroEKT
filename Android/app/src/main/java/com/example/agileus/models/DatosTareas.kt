package com.example.agileus.models

import com.google.gson.annotations.SerializedName

data class DatosTareas(
    @SerializedName("id_emisor")val id_emisor:String ,
    @SerializedName("id_receptor") var idReceptor: String,
    @SerializedName("fecha_ini")val fecha_ini:String ,
    @SerializedName("fecha_fin")val fecha_fin:String ,
    @SerializedName("fecha_finR")val fecha_finR:String ,
    @SerializedName("estatus")val status:String ,
    @SerializedName("leido")val leido:Boolean
)