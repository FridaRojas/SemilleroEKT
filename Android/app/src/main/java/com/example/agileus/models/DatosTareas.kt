package com.example.agileus.models

import com.google.gson.annotations.SerializedName

data class DatosTareas(

    @SerializedName("fecha")val fecha: String,
    @SerializedName("id")val id: String,
    @SerializedName("idconversacion")val idconversacion: String,
    @SerializedName("idemisor")val idemisor: String,
    @SerializedName("idreceptor")val idreceptor: String,
    @SerializedName("texto")val texto: String,
    @SerializedName("visible")val visible: Boolean,
    val imagen: Int

)