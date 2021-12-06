package com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.BuzonAdapter
import com.example.agileus.adapters.BuzonAdapterResponse
import com.example.agileus.models.*
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listafiltrada
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listaus
import com.example.agileus.utils.Constantes
import com.example.agileus.utils.Constantes.broadlist
import com.example.agileus.webservices.dao.MessageDao
import com.example.agileus.webservices.dao.ProviderBuzon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ReceiverBuzonBroadcastViewModel : ViewModel() {


    var adaptador = MutableLiveData<BuzonAdapterResponse>()
    var myResponse: MutableLiveData<Response<BuzonComunicados>> = MutableLiveData()

    var lista1: DaoBuzon1
       var conv: MessageDao


    init {
        lista1 = DaoBuzon1()
        conv = MessageDao()


    }

    companion object {
        var salas= ArrayList<String>()
        var listachats = ArrayList<Chats>()
        var listaBrd1=ArrayList<Chats>()
        var mensajes =ArrayList<BuzonComunicados>()
    }

    fun getLista():ArrayList<String> {

        listafiltrada = ArrayList()
            viewModelScope.launch {
                listaus = withContext(Dispatchers.IO) {
                    lista1.recuperarListadeContactos(broadlist)
                }
                Log.d("tama","${listaus.size}")
                if (listaus.isNotEmpty()) {
                    listaus.forEach(){
                        if(it.nombreRol != "BROADCAST")
                            listafiltrada.add(it.nombre)
                    }
                }
            }
          return listafiltrada
    }



    fun devuelvebuzon2() {
        try {
            viewModelScope.launch {
                 listachats = withContext(Dispatchers.IO) {
                    lista1.recuperarMensajesBrd1(broadlist)
                }
                if (listachats.isNotEmpty()) {
                    for (i in 0 until listachats.size) {
                        salas.add(listachats[i].idConversacion)
                        }
                    }
                getEnviados(salas)
            }
        } catch (ex: Exception) {
            Log.e("aqui", ex.message.toString())
        }
    }

     fun getEnviados(sala:ArrayList<String>) {

        try {
          for(i in 0 .. (sala.size)-1)
            viewModelScope.launch {
                mensajes = withContext(Dispatchers.IO) {
                    lista1.recuperarEnviadosBrd(sala[i])
                }

                if (mensajes.isNotEmpty()) {
                    mensajes.forEach {
                        it.idemisor="Broadcast"
                        var lista=getLista()
                        Log.d("lista 1",lista.size.toString())
                    }
                    adaptador.value = BuzonAdapterResponse(mensajes, 1)
                }
            }
        } catch (ex: Exception) {
            Log.e("aqui", ex.message.toString())
        }
    }




}