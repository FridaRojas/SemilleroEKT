package com.example.agileus.models

import com.google.gson.annotations.SerializedName

class Contacts (
    @SerializedName("id") val id: String,
    @SerializedName("correo") val correo: String,
    @SerializedName("fechaInicio") val fechaInicio: String,
    @SerializedName("fechaTermino") val fechaTermino: String,
    @SerializedName("numeroEmpleado")val numeroEmpleado: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("password") val password: String,
    @SerializedName("roles") val roles: String,
    @SerializedName("telefono")val telefono: String,
    @SerializedName("statusActivo") val statusActivo: String,
    @SerializedName("idsuperiorInmediato")val idsuperiorInmediato: String
    )