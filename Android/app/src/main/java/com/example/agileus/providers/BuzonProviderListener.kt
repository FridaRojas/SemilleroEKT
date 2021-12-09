package com.example.agileus.providers

import com.example.agileus.models.Buzon
import com.example.agileus.models.BuzonResp
import retrofit2.Response

interface BuzonProviderListener {
    suspend fun recibebuzon(ResponseDos: Response<ArrayList<Buzon>>, lista: ArrayList<Buzon>): ArrayList<Buzon>

}