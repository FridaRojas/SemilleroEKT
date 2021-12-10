package com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.BuzonAdapterResponse
import com.example.agileus.models.*
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.idUser
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listafiltrada
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listafiltrada1
import com.example.agileus.utils.Constantes.broadlist
import com.example.agileus.webservices.dao.MessageDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ReceiverBuzonBroadcastViewModel : ViewModel() {


    lateinit var listachats: List<Chats1>
    var adaptador = MutableLiveData<BuzonAdapterResponse>()
    var myResponse: MutableLiveData<Response<BuzonComunicados>> = MutableLiveData()

    var lista1: DaoBuzon1
    var conv: MessageDao


    init {
        lista1 = DaoBuzon1()
        conv = MessageDao()


    }

    companion object {
        var salas = ArrayList<String>()

        //      var listachats =
//        var listaBrd1
        var memsajes2 = ArrayList<Datas>()
        var mensajes = ArrayList<BuzonComunicados>()
    }

    fun getLista() {

        listafiltrada1 = ArrayList()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                lista1.recuperarListadeContactos(broadlist)
            }
            //    Log.d("tama","${listaus.size}")
            //               listaus.forEach {
            //            Log.d("lista name",it.nombre)
            //          Log.d("lista name",it.id)
            //                 listas.add(it)

        }
        //           if (listaus.isNotEmpty()) {
        //          listas = listaus   //
        //       }
        //}

    }


    fun devuelvebuzon2() {
    //    var listar=ArrayList<String>()
        try {
             viewModelScope.launch {
                 var  listar= withContext(Dispatchers.IO) {
                     lista1.recuperarMensajesBrd1(broadlist)
                }
                Log.d("lista", listar.toString())
                for (i in 0..listafiltrada.size - 1)
                    getEnviados(listafiltrada[i])
            }
        } catch (ex: Exception) {
            Log.e("aqui", ex.message.toString())
        }
    }

    suspend fun getEnviados(sala: String) {

//    try {
//////////////vamos bien
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                lista1.recuperarEnviadosBrd(idUser,sala)
            }

           val lista = getLista()
/*
        memsajes2.forEach {

                for (i in 0..lista.size - 1)
                      {
                        if(it.idreceptor==lista[i].id)
                        {
                         it.idreceptor=lista[i].nombre
                  }
          }*/
        }
          adaptador.value = BuzonAdapterResponse(memsajes2, 1)

        // }

        // } catch (ex: Exception) {
        //   Log.e("aqui", ex.message.toString())
        // }
    }
}


