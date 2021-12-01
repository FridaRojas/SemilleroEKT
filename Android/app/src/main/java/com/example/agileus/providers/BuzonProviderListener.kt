package com.example.agileus.providers

import com.example.agileus.models.Buzon
import com.example.agileus.models.ListaUsers
import retrofit2.Response

interface BuzonProviderListener {
    suspend fun recibebuzon(ResponseDos: Response<ArrayList<Buzon>>, lista:ArrayList<Buzon>): ArrayList<Buzon>
    suspend fun getLista(responseDos: Response<ArrayList<ListaUsers>>, lista: ArrayList<ListaUsers>): ArrayList<ListaUsers>

}