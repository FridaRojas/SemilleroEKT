package com.example.agileus.models

data class Data(
    val correo: String,
    val curp: String,
    val fechaInicio: String,
    val fechaTermino: String,
    val id: String,
    val idgrupo: String,
    val idsuperiorInmediato: String,
    val nombre: String,
    val nombreRol: String,
    val numeroEmpleado: String,
    val opcionales: List<Any>,
    val password: String,
    val rfc: String,
    val statusActivo: String,
    val telefono: String,
    val token: String
)