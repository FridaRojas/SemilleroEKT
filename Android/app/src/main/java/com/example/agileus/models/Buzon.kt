package com.example.agileus.Models

import com.google.gson.annotations.SerializedName

data class Buzon(

    @SerializedName ("senderId") var SenderId: String,
    @SerializedName ("receiverId") var receiverId: String,
    @SerializedName ("message") var message:String,
    @SerializedName ("asunto") var asunto:String
)
