package com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.models.Buzon
import com.example.agileus.adapters.BuzonAdapter
import com.example.agileus.models.ListaUsers
import com.example.agileus.webservices.dao.ProviderBuzon

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import kotlin.math.log

class BuzonDetallesViewModel : ViewModel() {

    lateinit var listafiltrada: ArrayList<Buzon>
    var adaptador = MutableLiveData<BuzonAdapter>()
    var myResponse: MutableLiveData<Response<Buzon>> = MutableLiveData()
    var lista: ProviderBuzon

    lateinit var listaConsumida: ArrayList<Buzon>
    lateinit var listausuarios: ArrayList<ListaUsers>

    init {
        lista = ProviderBuzon()
    }

    companion object {
        var listasize = 0
    }


    fun devuelvebuzon() {

        listaConsumida = ArrayList()

        try {
            viewModelScope.launch {
                listaConsumida = withContext(Dispatchers.IO) {
                        lista.recuperarbuzon(listaConsumida)
                }

                if (listaConsumida.isNotEmpty()) {
                    listasize = listaConsumida.size
                    listafiltrada = ArrayList()


                    if (BuzonFragment.control == 1) {
                        for (i in 0 until listaConsumida.size) {
                            if (listaConsumida[i].Receiverid == "Broadcast") {
                                listafiltrada.add(listaConsumida[i])
                            }
                        }
                    }

                    if (BuzonFragment.control == 2) {
                        for (i in 0 until listaConsumida.size) {
                            if (listaConsumida[i].Senderid == "Broadcast") {
                                listafiltrada.add(listaConsumida[i])
                            }
                        }
                    }
                    adaptador.value = BuzonAdapter(listafiltrada, BuzonFragment.control)
                }
            }
        } catch (ex: Exception) {
            Log.e("aqui", ex.message.toString())
        }
    }

       fun postMensaje(mypost: Buzon) {

        mypost.id = (listasize + 1).toString()
        mypost.Senderid

        try {
            viewModelScope.launch {
                val response = lista.pushPost(mypost)
                 myResponse.value=response
            }
        } catch (ex: Exception) {
            Log.e("aqui", ex.message.toString())
        }

     }

    fun getLista():ArrayList<ListaUsers> {
        listausuarios = ArrayList()
        try {
            viewModelScope.launch {
                listausuarios = withContext(Dispatchers.IO) {
                    lista.listausuarios(listausuarios)
                }

                if (listausuarios.isNotEmpty()) {
                    listasize = listausuarios.size
                    listafiltrada = ArrayList()
                }
            }
        } catch (ex: Exception) {
            Log.e("aqui", ex.message.toString())
        }
        return listausuarios
    }


}

