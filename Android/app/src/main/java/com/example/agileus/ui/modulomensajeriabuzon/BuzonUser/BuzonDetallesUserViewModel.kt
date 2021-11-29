package com.example.agileus.ui.modulomensajeriabuzon.BuzonUser

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.models.Buzon
import com.example.agileus.adapters.BuzonAdapter
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listasize
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonFragment.Companion.control
import com.example.agileus.webservices.dao.ProviderBuzon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class BuzonDetallesUserViewModel : ViewModel() {

    lateinit var listafiltrada: ArrayList<Buzon>
    var myResponse :MutableLiveData<Response<Buzon>> = MutableLiveData()
    var adaptador = MutableLiveData<BuzonAdapter>()
    var lista : ProviderBuzon
    lateinit var listaConsumida:ArrayList<Buzon>


    init {
        lista = ProviderBuzon()
    }

    fun devuelvebuzon(){

        var Actuser:String
        Actuser="Eduardo"

        listaConsumida= ArrayList()
        Log.i("tamaño","${listaConsumida.size}")
        try {
            viewModelScope.launch {
                listaConsumida = withContext(Dispatchers.IO) {
                    lista.recuperarbuzon(listaConsumida)
                }
                if (listaConsumida.isNotEmpty()) {
                    listasize =listaConsumida.size
                    listafiltrada = ArrayList()


                    if (control == 1) {
                        for (i in 0 until listaConsumida.size) {
                            if (listaConsumida[i].Senderid.toString() == "Eduardo") {
                                listafiltrada.add(listaConsumida[i])
                            }
                        }

                    }
                    if (control == 2) {
                        for (i in 0 until listaConsumida.size) {
                            if (listaConsumida[i].Receiverid == "General" || listaConsumida[i].Receiverid == Actuser) {
                                listafiltrada.add(listaConsumida[i])
                            }
                        }
                    }
                    adaptador.value = BuzonAdapter(listafiltrada, control)
                }
            }

        }catch (ex:Exception){
            Log.e("aqui", ex.message.toString())
        }
    }

    fun postMensaje(mypost: Buzon) {

        mypost.id=(listasize +1).toString()
        mypost.Senderid="Eduardo"


        try {
            viewModelScope.launch {
                val response : Response<Buzon> = lista.pushPost(mypost)
                myResponse.value=response
            }
        } catch (ex: Exception) {
            Log.e("aqui", ex.message.toString())
        }

    }


}