package com.example.agileus.ui.modulomensajeriabuzon.b

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.Models.Buzon
import com.example.agileus.adapters.BuzonAdapter
import com.example.agileus.ui.modulomensajeriabuzon.b.BuzonFragment.Companion.control
import com.example.agileus.webservices.dao.ProviderBuzon
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BuzonDetallesUserViewModel : ViewModel() {

    lateinit var listafiltrada: ArrayList<Buzon>
    var adaptador = MutableLiveData<BuzonAdapter>()
    var lista : ProviderBuzon
    lateinit var listaConsumida:ArrayList<Buzon>


    init {
        lista = ProviderBuzon()
    }

    fun devuelvebuzon(){
        listaConsumida= ArrayList()
        Log.i("tamaño","${listaConsumida.size}")
        try {
            viewModelScope.launch {
                listaConsumida = withContext(Dispatchers.IO) {
                    lista.recuperarbuzon(listaConsumida)
                }
                if (listaConsumida.isNotEmpty()) {
                    listafiltrada = ArrayList()


                    if (control == 1) {
                        listaConsumida.forEach()
                        {
                            Log.i("campo: id"," ${it.id}")
                            Log.i("campo: Receiver id"," ${it.Receiverid}")
                            Log.i("campo: Sender id"," ${it.Senderid}")
                            Log.i("campo: Asunto"," ${it.Asunto}")
                            Log.i("campo: Message"," ${it.Message}")
                        }

                        for (i in 0 until listaConsumida.size) {
                            if (listaConsumida[i].Senderid.toString() == "juan") {
                                listafiltrada.add(listaConsumida[i])
                            }
                        }
                        Log.i("size "," ${listafiltrada.size}")
                    }
                    if (control == 2) {
                        for (i in 0 until listaConsumida.size) {
                            if (listaConsumida[i].Receiverid == "General" || listaConsumida[i].Receiverid == "Pedro") {
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

}