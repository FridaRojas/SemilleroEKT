package com.example.agileus.models

import com.google.gson.annotations.SerializedName

data class DatosBroadCast(
    @SerializedName("id")val id: String,
    @SerializedName("asunto")val asunto: String,
    @SerializedName("descripcion")val descripcion: String,
    @SerializedName("idEmisor")val idEmisor: String,
    @SerializedName("nombreEmisor")val nombreEmisor: String,
    @SerializedName("atendido")val atendido: Any,
    @SerializedName("idGrupo")val idGrupo: Any,
    @SerializedName("idBroadcast")val idBroadcast: Any
)

class BroadcastByID(
    @SerializedName("estatus") val estatus: String,
    @SerializedName("mensaje") val mensaje: String,
    @SerializedName("data") val data: ArrayList<DatosBroadCast>
)