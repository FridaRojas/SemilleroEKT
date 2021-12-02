package com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster

import android.hardware.biometrics.BiometricManager
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.models.Buzon
import com.example.agileus.adapters.BuzonAdapter
import com.example.agileus.models.ListaUsers
import com.example.agileus.models.MensajeBodyBroadcaster
import com.example.agileus.models.MsgBodyUser
import com.example.agileus.utils.Constantes.broadlist
import com.example.agileus.webservices.dao.DaoBuzon
import com.example.agileus.webservices.dao.ProviderBuzon

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import kotlin.math.log

class BuzonDetallesViewModel : ViewModel() {

    lateinit var listafiltrada: ArrayList<Buzon>

    var adaptador = MutableLiveData<BuzonAdapter>()
    //var myResponse: MutableLiveData<Response<Buzon>> = MutableLiveData()
    var myResponse: MutableLiveData<Response<MensajeBodyBroadcaster>> = MutableLiveData()
    var myResponse1: MutableLiveData<Response<MsgBodyUser>> = MutableLiveData()

    var lista: ProviderBuzon
    var lista1: DaoBuzon1

    lateinit var listaConsumida: ArrayList<Buzon>
    lateinit var listausuarios: ArrayList<ListaUsers>

    init {
        lista = ProviderBuzon()
        lista1 = DaoBuzon1()
    }

    companion object {
        var listaus=ArrayList<ListaUsers>()
        var listasize = 0
        var listafiltrada1= ArrayList<String>()
    }

    /*
    fun devuelvebuzon() {

        listaConsumida = ArrayList()

        try {
            viewModelScope.launch {
                listaConsumida = withContext(Dispatchers.IO) {
                        lista.recuperarbuzon(listaConsumida)
                }

                if (listaConsumida.isNotEmpty()) {
                    listasize = listaConsumida.size
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

*/
    /*
     fun postMensaje(mypost: Buzon) {
        mypost.id = (listasize + 1).toString()
        mypost.Senderid

        try {
            viewModelScope.launch {
                val response = lista.pushPost(mypost)
                 myResponse.value=response
            }
        } catch (ex: Exception) {
            Log.e("aqui", ex.message.toString())
        }

     }
*/


       fun postMensaje(mypost: MensajeBodyBroadcaster) {

           viewModelScope.launch {
                 val response: Response<MensajeBodyBroadcaster> = lista1.getcustompost(mypost)
                 myResponse.value = response
             }
            }

    fun postRequest(mypost: MsgBodyUser) {

        viewModelScope.launch {
            val response: Response<MsgBodyUser> = lista1.getcustompush(mypost)
            myResponse1.value = response
        }
    }



    fun getLista():ArrayList<ListaUsers> {
        listausuarios = ArrayList()
        listafiltrada1 = ArrayList()
        try {
            viewModelScope.launch {
                listausuarios = withContext(Dispatchers.IO) {
                    lista1.recuperarListadeContactos(broadlist)
                }
                if (listausuarios.isNotEmpty()) {
                    listausuarios.forEach(){
                        listafiltrada1.add(it.nombre)
                    }
                }
            }
        } catch (ex: Exception) {
            Log.e("aqui", ex.message.toString())
        }
        return listausuarios
    } }

