package com.example.agileus.models.response

import com.example.agileus.models.Contacts
import com.google.gson.annotations.SerializedName

data class ResponseContacts(
    @SerializedName("status") val status:String,
    @SerializedName("msj") val msj:String,
    @SerializedName("data") var data:ArrayList<Contacts>
)