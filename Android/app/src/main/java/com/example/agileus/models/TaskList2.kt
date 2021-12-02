package com.example.agileus.models

import com.google.gson.annotations.SerializedName

data class TaskList2(
    @SerializedName("estatus") var estatus: String,
    @SerializedName("mensaje") var mensaje: String,
    @SerializedName("data") var data: DataTask
)