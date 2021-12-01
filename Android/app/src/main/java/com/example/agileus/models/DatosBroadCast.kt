package com.example.agileus.models

import com.google.gson.annotations.SerializedName

data class DatosBroadCast(
    @SerializedName("asunto")val asunto: String,
    @SerializedName("descripcion")val descripcion: String,
    @SerializedName("id")val id: String,
    @SerializedName("idEmisor")val idEmisor: String,
    @SerializedName("nombreEmisor")val nombreEmisor: String
)