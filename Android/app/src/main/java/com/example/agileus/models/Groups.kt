package com.example.agileus.models

import com.google.gson.annotations.SerializedName

data class Groups (
    @SerializedName("idConversacion") val idConversacion: String,
    @SerializedName("idReceptor") val idReceptor: String,
    @SerializedName("nombreConversacionRecepto") val nombreConversacionRecepto: String
        )

data class ResponseGroups(
    @SerializedName("status") val status:String,
    @SerializedName("msj") val msj:String,
    @SerializedName("data") var data:ArrayList<Groups>
)

