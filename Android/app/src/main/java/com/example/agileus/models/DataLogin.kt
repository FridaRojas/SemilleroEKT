package com.example.agileus.models

import com.google.gson.annotations.SerializedName

data class DataLogin (
    @SerializedName("id") var id : String? = null,
    @SerializedName("correo") var correo : String? = null,
    @SerializedName("fechaInicio") var fechaInicio : String? = null,
    @SerializedName("fechaTermino") var fechaTermino : String? = null,
    @SerializedName("numeroEmpleado") var numeroEmpleado : String? = null,
    @SerializedName("nombre") var nombre : String? = null,
    @SerializedName("password") var password : String? = null,
    @SerializedName("nombreRol") var nombreRol : String? = null,
    @SerializedName("opcionales") var opcionales : String? = null,
    @SerializedName("token") var token : String? = null,
    @SerializedName("telefono") var telefono : String? = null,
    @SerializedName("statusActivo") var statusActivo : String? = null,
    @SerializedName("curp") var curp : String? = null,
    @SerializedName("rfc") var rfc : String? = null,
    @SerializedName("tokenAuth") var tokenAuth : String? = null ,
    @SerializedName("idgrupo") var idgrupo : String? = null,
    @SerializedName("idsuperiorInmediato") var idsuperiorInmediato : String? = null,
        )