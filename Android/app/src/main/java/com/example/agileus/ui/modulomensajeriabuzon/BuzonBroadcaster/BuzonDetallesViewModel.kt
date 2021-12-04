package com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.BuzonAdapter
import com.example.agileus.adapters.BuzonAdapterResponse
import com.example.agileus.models.*
import com.example.agileus.utils.Constantes.broadlist
import com.example.agileus.webservices.dao.ProviderBuzon

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class BuzonDetallesViewModel : ViewModel() {

    lateinit var listafiltrada1: ArrayList<String>
    //lateinit var listafiltrada: ArrayList<Buzon>
    lateinit var listafiltrada2: ArrayList<BuzonResp>

    var adaptador = MutableLiveData<BuzonAdapter>()
    //var myResponse: MutableLiveData<Response<Buzon>> = MutableLiveData()
    var myResponse: MutableLiveData<Response<MensajeBodyBroadcaster>> = MutableLiveData()
    var myResponse1: MutableLiveData<Response<MsgBodyUser>> = MutableLiveData()

    var lista: ProviderBuzon
    var lista1: DaoBuzon1

    lateinit var listaConsumida: ArrayList<Buzon>
    lateinit var listaConsumida1: ArrayList<BuzonResp>
    lateinit var listausuarios: ArrayList<ListaUsers>

    init {
        lista = ProviderBuzon()
        lista1 = DaoBuzon1()
    }

    companion object {
        var listaus=ArrayList<ListaUsers>()
        var listaBrd=ArrayList<BuzonResp>()

        var listasize = 0
        var listafiltrada= ArrayList<String>()

    }

    fun getLista():ArrayList<ListaUsers> {
        listausuarios = ArrayList()
        listafiltrada1 = ArrayList()

        try {
            viewModelScope.launch {
                listausuarios = withContext(Dispatchers.IO) {
                    lista1.recuperarListadeContactos(broadlist)
                }
                Log.d("tam","${listausuarios.size}")
                if (listausuarios.isNotEmpty()) {
                    listausuarios.forEach(){
                        listafiltrada.add(it.nombre)
                    }
                }
            }
        } catch (ex: Exception) {
            Log.e("aqui", ex.message.toString())
        }
        return listausuarios
    }



    fun devuelvebuzon1() {

        listaConsumida1 = ArrayList()
        listafiltrada2=  ArrayList()
        listafiltrada1=  ArrayList()

        try {
            viewModelScope.launch {
                listaConsumida1 = withContext(Dispatchers.IO) {
                    lista1.recuperarMensajesBrd(broadlist)
                }

                if (listaConsumida1.isNotEmpty()) {
                    listasize = listaConsumida1.size
                    listafiltrada2 = ArrayList()

                    if (BuzonFragment.control == 1) {
                        for (i in 0 until listaConsumida1.size) {
                        //    if (listaConsumida[i].Receiverid == "Broadcast") {
                                listafiltrada2.add(listaConsumida1[i])
                            }
                        }
                    }

                    if (BuzonFragment.control == 2) {
                        for (i in 0 until listaConsumida.size) {
                            if (listaConsumida[i].Senderid == broadlist) {
                                listafiltrada2.add(listaConsumida1[i])
                            }
                        }
                    }
                    adaptador.value = BuzonAdapter(listafiltrada2, BuzonFragment.control)
                }
        } catch (ex: Exception) {
            Log.e("aqui", ex.message.toString())
        }
    }



}

