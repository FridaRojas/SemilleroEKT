package com.example.agileus.models.response

import com.example.agileus.models.Conversation
import com.google.gson.annotations.SerializedName

data class ResponseConversation(
    @SerializedName("status") val status:String,
    @SerializedName("msj") val msj:String,
    @SerializedName("data") var data:ArrayList<Conversation>
)