package com.example.agileus.ui.login.data.model

import com.google.gson.annotations.SerializedName

data class UsuariosResponse (

    @SerializedName("id") var id: String,
    @SerializedName("nombre") var nombre: String,
    @SerializedName("usuarios") var usuarios: ArrayList<Usuarios>

)

data class Usuarios(
    @SerializedName("id") var idUsuario: String,
    @SerializedName("correo") var correo:String,
    @SerializedName("numeroEmpleado") var numeroEmpleado:String,
    @SerializedName("nombre") var nombre:String,
    @SerializedName("password") var password:String,
    @SerializedName("nombreRol") var nombreRol:String,
    @SerializedName("idGrupo") var idGrupo:String,

)
