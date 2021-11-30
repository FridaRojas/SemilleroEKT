package com.example.agileus.ui.login.provider

import com.example.agileus.ui.login.data.model.Data
import retrofit2.Response

interface LoginProviderListener {
    suspend fun obtenerLista(listaConsumida: ArrayList<Data>, respuesta2 : Response<ArrayList<Data>>):ArrayList<Data>
    //suspend fun recupera(respuesta:Response<ArrayList<Data>>, lista: ArrayList<Data>) : ArrayList<Data>


}
