package com.example.agileus.models.response

import com.example.agileus.models.Groups
import com.google.gson.annotations.SerializedName

data class ResponseGroups(
    @SerializedName("status") val status:String,
    @SerializedName("msj") val msj:String,
    @SerializedName("data") var data:ArrayList<Groups>
)