package com.example.agileus.providers

import com.example.agileus.Models.Buzon
import retrofit2.Response

interface BuzonProviderListener {
    suspend fun recibebuzon(ResponseDos: Response<ArrayList<Buzon>>,lista:ArrayList<Buzon>): ArrayList<Buzon>

}