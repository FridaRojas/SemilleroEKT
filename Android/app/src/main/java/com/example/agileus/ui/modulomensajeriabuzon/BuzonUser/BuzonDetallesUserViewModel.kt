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
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listafiltrada
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listasize
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonFragment
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.DaoBuzon1
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.ReceiverBuzonBroadcastViewModel
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.ReceiverBuzonBroadcastViewModel.Companion.memsajes2
//import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.ReceiverBuzonBroadcastViewModel.Companion.listaBrd1
//import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.ReceiverBuzonBroadcastViewModel.Companion.listachats
//import com.example.agileus.utils.Constantes.CURRENT_USER
import com.example.agileus.utils.Constantes.broadlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class BuzonDetallesUserViewModel : ViewModel() {


    var myResponse1: MutableLiveData<Response<MsgBodyUser>> = MutableLiveData()
    var adaptador = MutableLiveData<BuzonAdapterResponse>()


    var lista: DaoBuzon1 = DaoBuzon1()


    ///
    fun devuelvebuzon2() {
                try {
            viewModelScope.launch {
                 withContext(Dispatchers.IO) {
                    lista.recuperarMensajesBrd1(idUser)
                }
                for (i in 0 until listafiltrada.size)
                    getEnviados(listafiltrada[i])
            }
        } catch (ex: Exception) {
            Log.e("aqui", ex.message.toString())
        }
    }

    suspend fun getEnviados(sala: String) {


        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                lista.recuperarEnviadosBrd(idUser, sala)
            }
            adaptador.value = BuzonAdapterResponse(memsajes2, 1)
        }
    }


    fun postRequest(mypost: MsgBodyUser) {
        viewModelScope.launch {
            val response: Response<MsgBodyUser> = lista.getcustompush(mypost)
            myResponse1.value = response
        }
    }
}

