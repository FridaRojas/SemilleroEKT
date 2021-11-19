package com.example.agileus.webservices.dao

import com.example.agileus.Models.Buzon
import com.example.agileus.providers.BuzonProviderListener
import retrofit2.Call
import retrofit2.Response

class DaoBuzon(var listener: BuzonProviderListener) {

    suspend fun obtener(callRespuesta: Call<ArrayList<Buzon>>, lista: ArrayList<Buzon>):ArrayList<Buzon>
    {
        var ResponseDos: Response<ArrayList<Buzon>> = callRespuesta.execute()
        var lista2=listener.recibebuzon(ResponseDos,lista)
        return lista2
    }

}