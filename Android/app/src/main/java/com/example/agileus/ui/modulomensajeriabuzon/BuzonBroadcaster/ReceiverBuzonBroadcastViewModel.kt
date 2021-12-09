package com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.BuzonAdapterResponse
import com.example.agileus.models.*
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listafiltrada
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listaus
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.ReceiverBuzonBroadcastFragment.Companion.listas
import com.example.agileus.utils.Constantes.broadlist
import com.example.agileus.webservices.dao.MessageDao
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

    fun getLista(): ArrayList<Contacts> {

        listafiltrada = ArrayList()
            viewModelScope.launch {
                listaus = withContext(Dispatchers.IO) {
                    lista1.recuperarListadeContactos(broadlist)
                }
                Log.d("tama","${listaus.size}")
                listaus.forEach {
                    Log.d("lista name",it.nombre)
                    Log.d("lista name",it.id)
                    listas.add(it)

                }
                if (listaus.isNotEmpty()) {
                 listas = listaus   //
                }
            }
          return listaus
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
                var lista=getLista()
                getEnviados(salas,lista)
            }
        } catch (ex: Exception) {
            Log.e("aqui", ex.message.toString())
        }
    }

     fun getEnviados(sala: ArrayList<String>, lista: ArrayList<Contacts>) {

        try {
          for(i in 0 .. (sala.size)-1)
            viewModelScope.launch {
                mensajes = withContext(Dispatchers.IO) {
                    lista1.recuperarEnviadosBrd(sala[i])
                }


                if (mensajes.isNotEmpty()) {
                    for(i in 0 until lista.size)
                    {
                        mensajes.forEach {
                            if (it.idreceptor == lista[i].id)
                             it.idreceptor=lista[i].nombre
                        }
                    }
                    mensajes.forEach {
                        it.idemisor="Broadcast"
                    }
                    adaptador.value = BuzonAdapterResponse(mensajes, 1)
                }
            }
        } catch (ex: Exception) {
            Log.e("aqui", ex.message.toString())
        }
    }




}