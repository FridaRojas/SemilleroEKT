package com.example.agileus.Models

import com.google.gson.annotations.SerializedName

data class Buzon (
    var SenderId:String,
    var receiverId:String,
    var message:String,
    var asunto:String,
    var fecha:String,
    @SerializedName("id")val id: String
)
