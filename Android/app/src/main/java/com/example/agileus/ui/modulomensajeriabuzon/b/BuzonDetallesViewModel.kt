package com.example.agileus.ui.modulomensajeriabuzon.b

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.Models.Buzon
import com.example.agileus.adapters.BuzonAdapter
import com.example.agileus.config.InitialApplication
import com.example.agileus.webservices.apis.BuzonApi
import com.example.agileus.webservices.dao.ProviderBuzon

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class BuzonDetallesViewModel : ViewModel() {

    lateinit var listafiltrada: ArrayList<Buzon>
    var adaptador = MutableLiveData<BuzonAdapter>()
    var myResponse :MutableLiveData<Response<Buzon>> = MutableLiveData()
    var lista: ProviderBuzon
    lateinit var listaConsumida: ArrayList<Buzon>

    init {
        lista = ProviderBuzon()
    }

    companion object
    {
        var listasize=1
    }


    fun devuelvebuzon() {
        listaConsumida = ArrayList()
        try {
            viewModelScope.launch {
                listaConsumida = withContext(Dispatchers.IO) {
                    lista.recuperarbuzon(listaConsumida)
                }

                if (listaConsumida.isNotEmpty()) {
                    listasize=listaConsumida.size
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

        mypost.id=(listasize+1).toString()
        mypost.Senderid.toString()

        try {
            viewModelScope.launch {
                val response :Response<Buzon> = lista.pushPost(mypost)
                 myResponse.value=response
            }
        } catch (ex: Exception) {
            Log.e("aqui", ex.message.toString())
        }

    }

}

