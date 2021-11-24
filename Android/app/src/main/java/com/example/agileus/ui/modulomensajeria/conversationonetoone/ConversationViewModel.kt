package com.example.agileus.ui.modulomensajeria.listacontactos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.ConversationAdapter
import com.example.agileus.models.Contacts
import com.example.agileus.models.Conversation
import com.example.agileus.models.Message
import com.example.agileus.ui.modulomensajeria.listaconversations.ListConversationViewModel
import com.example.agileus.ui.modulomensajeria.listcontacts.ListContactsViewModel
import com.example.agileus.webservices.dao.MessageDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConversationViewModel:ViewModel() {

    var adaptador = MutableLiveData<ConversationAdapter>()
    lateinit var message:MessageDao
    lateinit var listaConsumida:ArrayList<Conversation>


    init {

        message = MessageDao()
    }

    fun devuelveLista(){
        try {
            viewModelScope.launch {
                listaConsumida =  withContext(Dispatchers.IO) {
                message.recuperarMensajes()
                }
                if (listaConsumida != null){
                    if(listaConsumida.isNotEmpty()){
                        adaptador.value = ConversationAdapter(listaConsumida as ArrayList<Conversation>)
                    }
                }

            }


        }catch (ex:Exception){
            Log.e(ListConversationViewModel::class.simpleName.toString(), ex.message.toString())
        }


    }


    fun mandarMensaje(mensaje: Message){
        try {
            viewModelScope.launch {
                message.insertarMensajes(mensaje)
                listaConsumida =  withContext(Dispatchers.IO) {
                   message.recuperarMensajes()
                }
                if (listaConsumida != null){
                    if(listaConsumida.isNotEmpty()){
                        adaptador.value!!.update(listaConsumida)
                    }
                }

            }
        }catch (ex:Exception){
            Log.e(ListContactsViewModel::class.simpleName.toString(), ex.message.toString())
        }

    }

}