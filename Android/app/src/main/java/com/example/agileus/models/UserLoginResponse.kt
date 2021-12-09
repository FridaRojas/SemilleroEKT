package com.example.agileus.models

import com.example.agileus.ui.login.data.model.Usuarios
import com.google.gson.annotations.SerializedName

data class UserLoginResponse (
        @SerializedName("id") var id: String,
        @SerializedName("nombre") var nombre: String,
        @SerializedName("usuarios") var usuarios: Any
        )