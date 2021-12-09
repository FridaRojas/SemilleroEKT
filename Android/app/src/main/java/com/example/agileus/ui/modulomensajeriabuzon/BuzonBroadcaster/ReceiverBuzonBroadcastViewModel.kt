package com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.BuzonAdapter
import com.example.agileus.adapters.BuzonAdapterResponse
import com.example.agileus.models.*
import com.example.agileus.utils.Constantes
import com.example.agileus.utils.Constantes.broadlist
import com.example.agileus.webservices.dao.ProviderBuzon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ReceiverBuzonBroadcastViewModel : ViewModel() {

    var adaptador = MutableLiveData<BuzonAdapterResponse>()
    var myResponse: MutableLiveData<Response<BuzonComunicados>> = MutableLiveData()

    var lista1: DaoBuzon1


    init {
        lista1 = DaoBuzon1()
    }

    companion object {
        var listaBrd1=ArrayList<BuzonComunicados>()
    }


    fun devuelvebuzon2() {


        try {
            viewModelScope.launch {
                listaBrd1 = withContext(Dispatchers.IO) {
                    lista1.recuperarMensajesBrd1(broadlist)
                }

                if (listaBrd1.isNotEmpty()) {
                    //       BuzonDetallesViewModel.listasize = listaConsumida1.size
                }
            }
            adaptador.value = BuzonAdapterResponse(listaBrd1, 1)

        } catch (ex: Exception) {
            Log.e("aqui", ex.message.toString())
        }
    }
}