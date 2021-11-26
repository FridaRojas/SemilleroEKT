package com.example.agileus.ui.modulomensajeria.listacontactos

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.ConversationAdapter
import com.example.agileus.models.Conversation
import com.example.agileus.models.Message
import com.example.agileus.models.MessageResponse
import com.example.agileus.ui.modulomensajeria.listaconversations.ListConversationViewModel
import com.example.agileus.webservices.dao.MessageDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConversationViewModel:ViewModel() {

    var adaptador = MutableLiveData<ConversationAdapter>()
    lateinit var message:MessageDao
    lateinit var listaConsumida:ArrayList<Conversation>
    lateinit var RespuestaMessage:MessageResponse
    var messages = MutableLiveData<ArrayList<Conversation>>()


    init {
        message = MessageDao()
    }

    fun devuelveLista(idChat:String){
        try {
            viewModelScope.launch {
                listaConsumida =  withContext(Dispatchers.IO) {
                message.recuperarMensajes(idChat)
                }
                if (listaConsumida != null){
                    if(listaConsumida.isNotEmpty()){
                        messages.value = listaConsumida
                        adaptador.postValue(ConversationAdapter(listaConsumida as ArrayList<Conversation>))

                    }
                }

            }
        }catch (ex:Exception){
            Log.e(ListConversationViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }


    fun mandarMensaje(idChat:String,mensaje: Message){
        try {
            viewModelScope.launch {
                RespuestaMessage = withContext(Dispatchers.IO) {
                    message.insertarMensajes(mensaje)
                }
                devuelveLista(idChat)
            }
        }catch (ex:Exception){
            Log.e(ListConversationViewModel::class.simpleName.toString(), ex.message.toString())
        }

    }

}