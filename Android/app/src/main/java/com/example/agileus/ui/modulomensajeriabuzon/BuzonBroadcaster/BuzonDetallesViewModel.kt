package com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.BuzonAdapter
import com.example.agileus.models.*
import com.example.agileus.utils.Constantes.broadlist
import com.example.agileus.webservices.dao.ProviderBuzon

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class BuzonDetallesViewModel : ViewModel() {

    lateinit var listafiltrada1: ArrayList<String>

    //lateinit var listafiltrada: ArrayList<Buzon>
    lateinit var listafiltrada2: ArrayList<DataBuzon>

    var adaptador = MutableLiveData<BuzonAdapter>()

    //var myResponse: MutableLiveData<Response<Buzon>> = MutableLiveData()
    var myResponse: MutableLiveData<Response<MensajeBodyBroadcaster>> = MutableLiveData()
    var myResponse1: MutableLiveData<Response<MsgBodyUser>> = MutableLiveData()

    var lista: ProviderBuzon
    var lista1: DaoBuzon1

    lateinit var listaConsumida: ArrayList<DataBuzon>
    lateinit var listaConsumida1: ArrayList<DataBuzon>

    init {
        lista = ProviderBuzon()
        lista1 = DaoBuzon1()
    }

    companion object {

        //        var listaconversaciones=ArrayList<S>
        var listaus = ArrayList<Datos>()
        var listaBrd = ArrayList<DataBuzon>()
        var listasize = 0
        var listafiltrada = ArrayList<String>()
        var listafiltrada1 = ArrayList<String>()
    }

    fun getLista():ArrayList<String> {
        listafiltrada1 = ArrayList()
        Log.d("current user", broadlist)
        try {
            viewModelScope.launch {
                listaus = withContext(Dispatchers.IO) {
                    lista1.recuperarListadeContactos(broadlist)
                }
                Log.d("usuarios totales", listaus.size.toString())

//                Log.d("tama","${listaus.size}")
                if (listaus.isNotEmpty())
                    listaus.forEach() {
                        listafiltrada1.add(it.nombre)
                    }
            }
        }

        catch (ex: Exception) {
            Log.e("aqui", ex.message.toString())
        }
    return listafiltrada1}


        fun devuelvebuzon1() {

            listaConsumida1 = ArrayList()
            listafiltrada2 = ArrayList()
//            listafiltrada1 = ArrayList()

            try {
                viewModelScope.launch {
                    listaConsumida1 = withContext(Dispatchers.IO) {
                        lista1.recuperarMensajesBrd(broadlist)
                    }

                    if (listaConsumida1.isNotEmpty()) {
                        listasize = listaConsumida1.size

                        Log.d("listaconsumida", listasize.toString())
                        adaptador.value = BuzonAdapter(listaConsumida1, BuzonFragment.control)
                    }}}
                        /*     if (BuzonFragment.control == 1) {
                                 for (i in 0 until listaConsumida1.size) {
                                     if (listaConsumida1[i].idEmisor == "Broadcast") {
                                         listafiltrada2.add(listaConsumida1[i])
                                     }
                                 }
                             }


                         if (BuzonFragment.control == 2) {
                             for (i in 0 until listaConsumida1.size) {
                                 if (listaConsumida1[i].idEmisor == broadlist) {
                                     listafiltrada2.add(listaConsumida1[i])
                                 }
                             }
                         }
                         }*/
            catch (ex: Exception) {
                Log.e("ERROR", ex.message.toString())
            }
        }

        fun postMensaje(mypost: MensajeBodyBroadcaster) {
            try {
                viewModelScope.launch {
                    val response: Response<MensajeBodyBroadcaster> = lista1.getcustompost(mypost)
                    myResponse.value = response
                }
            } catch (ex: Exception) {
                Log.e("aqui", ex.message.toString())
            }

        }
    }




