package com.example.agileus.ui.modulomensajeriabuzon.b

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.Models.Buzon
import com.example.agileus.adapters.BuzonAdapter
import com.example.agileus.webservices.dao.ProviderBuzon

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BuzonDetallesViewModel : ViewModel() {

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
                listaConsumida =  withContext(Dispatchers.IO) {
                    lista.recuperarbuzon(listaConsumida)
                }
                    if(listaConsumida.isNotEmpty()){
                        adaptador.value = BuzonAdapter(listaConsumida,1)
                    }
                }


        }catch (ex:Exception){
            Log.e("aqui", ex.message.toString())
        }
    }


    }

