package com.example.agileus.models.response

import com.example.agileus.models.Chats
import com.google.gson.annotations.SerializedName

data class ResponseChats(
    @SerializedName("status") val status:String,
    @SerializedName("msj") val msj:String,
    @SerializedName("data") var data:ArrayList<Chats>
)