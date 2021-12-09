package com.example.agileus.ui.modulomensajeriabuzon.BuzonUser

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.models.Buzon
import com.example.agileus.adapters.BuzonAdapter
import com.example.agileus.adapters.BuzonAdapterResponse
import com.example.agileus.models.BuzonComunicados
import com.example.agileus.models.BuzonResp
import com.example.agileus.models.MsgBodyUser
import com.example.agileus.ui.login.ui.login.InicioSesionFragment
import com.example.agileus.ui.login.ui.login.InicioSesionFragment.Companion.idUser
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listasize
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonFragment
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.DaoBuzon1
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.ReceiverBuzonBroadcastViewModel
//import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.ReceiverBuzonBroadcastViewModel.Companion.listaBrd1
//import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.ReceiverBuzonBroadcastViewModel.Companion.listachats
//import com.example.agileus.utils.Constantes.CURRENT_USER
import com.example.agileus.utils.Constantes.broadlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class BuzonDetallesUserViewModel : ViewModel() {

    lateinit var listaConsumida: ArrayList<Buzon>
    lateinit var listaConsumida1: ArrayList<BuzonResp>

    lateinit var listafiltrada1: ArrayList<String>
    //lateinit var listafiltrada: ArrayList<Buzon>
    lateinit var listafiltrada2: ArrayList<BuzonResp>


    var myResponse1 :MutableLiveData<Response<MsgBodyUser>> = MutableLiveData()
    var adaptador = MutableLiveData<BuzonAdapterResponse>()
    //var adaptador1 = MutableLiveData<BuzonAdapter>()

    var lista : DaoBuzon1 = DaoBuzon1()


    ///
    fun devuelvebuzon2() {
        //    var listar=ArrayList<String>()
        try {
            viewModelScope.launch {
                var  listar= withContext(Dispatchers.IO) {
                    lista.recuperarMensajesBrd1(idUser)
                }
                Log.d("lista", listar.toString())
                for (i in 0..BuzonDetallesViewModel.listafiltrada.size - 1)
                    getEnviados(BuzonDetallesViewModel.listafiltrada[i])
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
                lista.recuperarEnviadosBrd(InicioSesionFragment.idUser,sala)
            }
            adaptador.value = BuzonAdapterResponse(ReceiverBuzonBroadcastViewModel.memsajes2, 1)


//            val lista = getLista()
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

        // }

        // } catch (ex: Exception) {
        //   Log.e("aqui", ex.message.toString())
        // }
    }
    //

    fun postRequest(mypost: MsgBodyUser) {
        viewModelScope.launch {
            val response: Response<MsgBodyUser> = lista.getcustompush(mypost)
            myResponse1.value = response
        }
    }
}
