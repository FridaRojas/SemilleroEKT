package com.example.agileus.ui.modulomensajeriabuzon.BuzonUser

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.BuzonAdapterResponse
import com.example.agileus.models.MsgBodyUser
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.idUser
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listafiltrada
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.DaoBuzon1
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.ReceiverBuzonBroadcastViewModel.Companion.memsajes2
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

