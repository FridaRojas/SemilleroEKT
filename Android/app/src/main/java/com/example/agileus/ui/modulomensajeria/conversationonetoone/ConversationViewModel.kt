package com.example.agileus.ui.modulomensajeria.listacontactos

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.ChatsAdapter
import com.example.agileus.adapters.ConversationAdapter
import com.example.agileus.models.*
import com.example.agileus.ui.modulomensajeria.listaconversations.ListConversationViewModel
import com.example.agileus.utils.Constantes
import com.example.agileus.webservices.dao.MessageDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConversationViewModel:ViewModel() {
    var adaptador = MutableLiveData<ConversationAdapter>()
    lateinit var message: MessageDao
    lateinit var listaConsumida: ArrayList<Conversation>
    lateinit var RespuestaMessage: MessageResponse
    var responseM = MutableLiveData<MessageResponse>()
    var actualizar = MutableLiveData<ArrayList<Conversation>>()

    init {
        message = MessageDao()
    }

    fun devuelveLista(idUser:String, idChat: String) {
        try {
            viewModelScope.launch {
                listaConsumida = withContext(Dispatchers.IO) {
                    message.recuperarMensajes(idUser,idChat)
                }
                if (listaConsumida != null) {
                    if (listaConsumida.isNotEmpty()) {
                        adaptador.postValue(ConversationAdapter(listaConsumida as ArrayList<Conversation>))
                        actualizar.value = listaConsumida
                    }
                    else{

                    }
                }else{

                }
            }
        } catch (ex: Exception) {
            Log.e(ListConversationViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }

    fun mandarMensaje(idUser:String,idChat:String,mensaje: Message){

        try {
            viewModelScope.launch {
                RespuestaMessage = withContext(Dispatchers.IO) {
                    message.insertarMensajes(mensaje)
                }
                devuelveLista(idUser,idChat)
                responseM.value = RespuestaMessage
            }
        }catch (ex:Exception){
            Log.e(ListConversationViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }

    fun statusUpdateMessage(idUser:String,statusRead: StatusRead){
        try {
            viewModelScope.launch {
                RespuestaMessage = withContext(Dispatchers.IO) {
                    message.actualizarStatus(idUser,statusRead)
                }
                responseM.value = RespuestaMessage
            }
        }catch (ex:Exception){
            Log.e(ListConversationViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }

}