package com.example.agileus.webservices.dao

import com.example.agileus.models.Buzon
import com.example.agileus.models.ListaUsers
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

    suspend fun getlist(callRespuesta: Call<ArrayList<ListaUsers>>, lista: ArrayList<ListaUsers>):ArrayList<ListaUsers>
    {
        var ResponseDos: Response<ArrayList<ListaUsers>> = callRespuesta.execute()
        var lista2=listener.getLista(ResponseDos,lista)
        return lista2
    }


}