package com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.BuzonAdapterResponse
import com.example.agileus.models.*
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.id
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.idUser
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listafiltrada
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listafiltrada1
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listaus
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.ReceiverBuzonBroadcastFragment.Companion.listas
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
        var memsajes2 = ArrayList<Datas>()
        var mensajes = ArrayList<BuzonComunicados>()
     }

    fun getLista(){


        listas=ArrayList()

        listafiltrada1 = ArrayList()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                lista1.recuperarListadeContactos(broadlist)
            }
        }
        Log.d("tamaño", listas.size.toString())

    }

    fun devuelvebuzon2() {

        try {
             viewModelScope.launch {
                 var  listar= withContext(Dispatchers.IO) {
                     lista1.recuperarMensajesBrd1(id)
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
                lista1.recuperarEnviadosBrd(id, sala)
            }

            memsajes2.forEach {
                it.idemisor = "Broadcast"
            }
/*                 lista.forEach{
                   Log.d("names ",it.nombre)
                }
            }
*/
//                    if (it.idreceptor == lista[i].id) {
                 //       it.idreceptor = lista[i].nombre
                   //      Log.d("receptor",it.idreceptor)
                }
                adaptador.value = BuzonAdapterResponse(memsajes2, 1)

                 }

                // } catch (ex: Exception) {
                //   Log.e("aqui", ex.message.toString())
                // }
            }



