package com.example.agileus.ui.modulotareas.listatareas

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.ConversationAdapter
import com.example.agileus.models.Conversation
import com.example.agileus.ui.modulomensajeria.listacontactos.HomeViewModel
import com.example.agileus.webservices.dao.ConversationDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardViewModel : ViewModel() {

    var adaptador = MutableLiveData<ConversationAdapter>()
    lateinit var lista: ConversationDao

    //Cambiar el tipo de ArrayList a Tarea
    lateinit var listaConsumida: ArrayList<Conversation>

    init {
        lista = ConversationDao()
    }

    fun devuelveLista() {
        try {
            viewModelScope.launch {
                listaConsumida = withContext(Dispatchers.IO) {
                    lista.recuperarPublicaciones()
                }
                if (listaConsumida != null) {
                    if (listaConsumida.isNotEmpty()) {
                        adaptador.value =
                            ConversationAdapter(listaConsumida as ArrayList<Conversation>)
                    }
                }

            }


        } catch (ex: Exception) {
            Log.e(HomeViewModel::class.simpleName.toString(), ex.message.toString())
        }


    }

}