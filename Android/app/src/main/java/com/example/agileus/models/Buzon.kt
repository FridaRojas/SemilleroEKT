package com.example.agileus.Models

import com.google.gson.annotations.SerializedName

data class Buzon(
    @SerializedName ("id") var id: String,
    @SerializedName ("Senderid") var Senderid: String,
    @SerializedName ("Receiverid") var Receiverid: String,
    @SerializedName ("Message") var Message:String,
    @SerializedName ("Asunto") var Asunto:String
)
