package com.example.agileus.ui.login.data.model

import com.google.gson.annotations.SerializedName


data class User(
    val `data`: Data,
    val msj: String,
    val status: String
)

data class Data(
    val correo: String,
    val curp: Any,
    val fechaInicio: Any,
    val fechaTermino: Any,
    val id: String,
    val idgrupo: Any,
    val idsuperiorInmediato: Any,
    val nombre: String,
    val nombreRol: String,
    val numeroEmpleado: String,
    val opcionales: Any,
    val password: Any,
    val rfc: Any,
    val statusActivo: Any,
    val telefono: String,
    val token: String
)


/*data class User (

    @SerializedName("status" ) var status : String? = null,
    @SerializedName("msj"    ) var msj    : String? = null,
    @SerializedName("data"   ) var data   : Data?   = Data()

)

data class Data (

    @SerializedName("id"                  ) var id                  : String? = null,
    @SerializedName("correo"              ) var correo              : String? = null,
    @SerializedName("fechaInicio"         ) var fechaInicio         : String? = null,
    @SerializedName("fechaTermino"        ) var fechaTermino        : String? = null,
    @SerializedName("numeroEmpleado"      ) var numeroEmpleado      : String? = null,
    @SerializedName("nombre"              ) var nombre              : String? = null,
    @SerializedName("password"            ) var password            : String? = null,
    @SerializedName("nombreRol"           ) var nombreRol           : String? = null,
    @SerializedName("opcionales"          ) var opcionales          : String? = null,
    @SerializedName("token"               ) var token               : String? = null,
    @SerializedName("telefono"            ) var telefono            : String? = null,
    @SerializedName("statusActivo"        ) var statusActivo        : String? = null,
    @SerializedName("curp"                ) var curp                : String? = null,
    @SerializedName("rfc"                 ) var rfc                 : String? = null,
    @SerializedName("idgrupo"             ) var idgrupo             : String? = null,
    @SerializedName("idsuperiorInmediato" ) var idsuperiorInmediato : String? = null

)

 */