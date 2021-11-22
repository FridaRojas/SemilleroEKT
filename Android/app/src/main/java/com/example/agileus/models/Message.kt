package com.example.agileus.models

import com.google.gson.annotations.SerializedName

data class Message (
    @SerializedName("idEmisor") val idEmisor:String,
    @SerializedName("idReceptor") val idReceptor:String,
    @SerializedName("texto") val texto:String,
    @SerializedName("fechaCreacion") val fechaCreacion:String
)