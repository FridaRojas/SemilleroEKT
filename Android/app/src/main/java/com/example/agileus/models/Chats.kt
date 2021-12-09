package com.example.agileus.models

import com.google.gson.annotations.SerializedName

data class Chats (
    @SerializedName("idConversacion") val idConversacion: String,
    @SerializedName("idReceptor") val idReceptor: String,
    @SerializedName("nombreConversacionRecepto") val nombreConversacionRecepto: String,
    @SerializedName("idEmisor") val idEmisor: String,
    @SerializedName("nombreRol") val nombreRol: String
    )

data class Chats1(
    @SerializedName("status")val status: String,
    @SerializedName("msj") val msj: String,
    @SerializedName("data") val data: List<Data>
)

data class Data(
    @SerializedName("idConversacion") val idConversacion: String,
    @SerializedName("idReceptor") val idReceptor: String,
    @SerializedName("nombreConversacionRecepto") val nombreConversacionRecepto: String,
    @SerializedName("idEmisor") val idEmisor: String,
    @SerializedName("nombreRol") val nombreRol: String
)

