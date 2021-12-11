package com.example.agileus.webservices.apis

import com.example.agileus.models.LoginResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NivelApi{
    @GET("user/findByBossId/{id}")
    fun getUsersByBoss(@Path("id") id: String): Call<LoginResponse>?
}