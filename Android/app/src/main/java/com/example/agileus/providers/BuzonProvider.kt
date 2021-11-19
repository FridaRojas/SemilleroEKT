package com.example.agileus.providers

import com.example.agileus.Models.Buzon
import retrofit2.Call
import retrofit2.Response

class BuzonProvider(var listener: BuzonProviderListener) {
    suspend fun obtener(callRespuesta: Call<ArrayList<Buzon>>, lista: ArrayList<Buzon>):ArrayList<Buzon>
    {
        var ResponseDos: Response<ArrayList<Buzon>> = callRespuesta.execute()
        var lista2=listener.recibebuzon(ResponseDos,lista)
        return lista2
    }
}