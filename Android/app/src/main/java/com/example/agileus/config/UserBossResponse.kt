package com.example.agileus.config

import com.google.gson.annotations.SerializedName

data class UserBossResponse (
    @SerializedName("status")   val status: String,
    @SerializedName("msj")      val msj: String,
    @SerializedName("data")     val data: Any?

)
