package com.example.agileus.models

import com.google.gson.annotations.SerializedName

data class MessageResponse (
    @SerializedName("status") val status:String,
    @SerializedName("msj") val msj:String,
    @SerializedName("data") val data:String
)