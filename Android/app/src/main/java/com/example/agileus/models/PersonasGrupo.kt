package com.example.agileus.models

import com.google.gson.annotations.SerializedName

data class PersonasGrupo(
    @SerializedName("data")     val data: List<Datas>,
    @SerializedName("msj")      val msj: String,
    @SerializedName("status")   val status: String
)