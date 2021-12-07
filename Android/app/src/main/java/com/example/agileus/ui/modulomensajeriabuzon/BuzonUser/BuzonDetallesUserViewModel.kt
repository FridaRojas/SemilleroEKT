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
import com.example.agileus.ui.login.ui.login.InicioSesionFragment.Companion.idUser
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listasize
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonFragment
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonFragment.Companion.control
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.DaoBuzon1
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.ReceiverBuzonBroadcastViewModel
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.ReceiverBuzonBroadcastViewModel.Companion.listaBrd1
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.ReceiverBuzonBroadcastViewModel.Companion.listachats
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.ReceiverBuzonBroadcastViewModel.Companion.mensajes
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.ReceiverBuzonBroadcastViewModel.Companion.salas
import com.example.agileus.utils.Constantes.CURRENT_USER
import com.example.agileus.utils.Constantes.broadlist
import com.example.agileus.webservices.dao.ProviderBuzon
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


    var myResponse :MutableLiveData<Response<MsgBodyUser>> = MutableLiveData()

    var myResponse1 :MutableLiveData<Response<BuzonComunicados>> = MutableLiveData()

    var adaptador = MutableLiveData<BuzonAdapterResponse>()
    var adaptador1 = MutableLiveData<BuzonAdapter>()

    var lista : DaoBuzon1


    init {
 //       listaConsumida=ArrayList()
        lista =DaoBuzon1()
    }

    fun postRequest(mypost: MsgBodyUser) {
        viewModelScope.launch {
            val response: Response<MsgBodyUser> = lista.getcustompush(mypost)
            myResponse.value = response
        }
    }


    fun devuelvebuzon() {

        CURRENT_USER=idUser
        listaConsumida1 = ArrayList()
        listafiltrada2=  ArrayList()
        listafiltrada1=  ArrayList()


        try {
            viewModelScope.launch {
                listaConsumida1 = withContext(Dispatchers.IO) {
                    lista.recuperarMensajesBrd(CURRENT_USER)
                }

                if (listaConsumida1.isNotEmpty()) {
                    listasize = listaConsumida1.size
                    listafiltrada2 = ArrayList()
                    Log.d("size",listaConsumida1.size.toString())

                    for (i in 0 until listaConsumida1.size) {
                        if (listaConsumida1[i].idemisor == CURRENT_USER) {
                            listafiltrada2.add(listaConsumida1[i])
                        }
                    }

                    adaptador1.value = BuzonAdapter(listafiltrada2, BuzonFragment.control)
                }
            }
        } catch (ex: Exception) {
            Log.e("aqui", ex.message.toString())
        }
    }



    fun devuelvebuzonentrada() {///recibidos
        try {
            listachats= ArrayList()
            viewModelScope.launch {
                listachats = withContext(Dispatchers.IO) {
                    lista.recuperarMensajesBrd1(broadlist)
                }
                if (listachats.isNotEmpty()) {
                    for (i in 0 until listachats.size) {
                        if(listachats[i].idReceptor == CURRENT_USER) {///
                            salas.add(listachats[i].idConversacion)
                            Log.d("salitas ",listachats[i].idConversacion.toString())
                        }
                        Log.d("salitas ",listachats[i].idConversacion.toString())
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
                        lista.recuperarEnviadosBrd(sala[i])
                    }
                    if (mensajes.isNotEmpty()) {
                        mensajes.forEach {
                            it.idreceptor="Broadcast"
                        }
                        adaptador.value = BuzonAdapterResponse(mensajes, 1)
                    }
                }

        } catch (ex: Exception) {
            Log.e("aqui", ex.message.toString())
        }
    }

}
