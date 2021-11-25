package com.example.agileus.models

import com.google.gson.annotations.SerializedName

data class DatosTareas(
    @SerializedName("id_grupo")val id_grupo: String,
    @SerializedName("id_emisor")val id_emisor:String ,
    @SerializedName("nombre_emisor")val nombre_emisor:String ,
    @SerializedName("id_receptor")val id_receptor: String,
    @SerializedName("nombre_receptor")val nombre_receptor:String,
    @SerializedName("fecha_ini")val fecha_ini:String ,
    @SerializedName("fecha_fin")val fecha_fin:String ,
    @SerializedName("titulo")val titulo:String ,
    @SerializedName("descripcion")val descripcion:String ,
    @SerializedName("prioridad")val prioridad:String ,
    @SerializedName("status")val status:String ,
    @SerializedName("leido")val leido:Boolean ,
    @SerializedName("createdDate")val createdDate:String ,
    @SerializedName("observaciones")val observaciones:String
)