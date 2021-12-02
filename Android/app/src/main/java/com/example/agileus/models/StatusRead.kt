package com.example.agileus.models

import com.google.gson.annotations.SerializedName

data class StatusRead(
    @SerializedName("id") val id: String,
    @SerializedName("fechaLeido") val fechaLeido: String
)
