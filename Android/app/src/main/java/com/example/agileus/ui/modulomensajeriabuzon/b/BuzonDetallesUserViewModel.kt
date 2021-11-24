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
                        for (i in 0 until listaConsumida.size) {
                            if (listaConsumida[i].SenderId == "Eduardo") {
                                listafiltrada.add(listaConsumida[i])
                            }
                        }
                    }

                    if (control == 2) {
                        for (i in 0 until listaConsumida.size) {
                            if (listaConsumida[i].receiverId == "General" || listaConsumida[i].receiverId == "Eduardo") {
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