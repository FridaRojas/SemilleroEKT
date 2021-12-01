package com.example.agileus.models


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