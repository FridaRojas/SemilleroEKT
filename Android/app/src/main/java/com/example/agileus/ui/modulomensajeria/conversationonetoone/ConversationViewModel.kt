package com.example.agileus.ui.modulomensajeria.conversationonetoone

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.ConversationAdapter
import com.example.agileus.models.Conversation
import com.example.agileus.models.Message
import com.example.agileus.ui.modulomensajeria.listaconversaciones.ListConversationViewModel
import com.example.agileus.webservices.dao.ConversationDao
import com.example.agileus.webservices.dao.MessageDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConversationViewModel:ViewModel() {

    var adaptador = MutableLiveData<ConversationAdapter>()
    lateinit var lista : ConversationDao
    lateinit var peticionMensaje:MessageDao
    lateinit var listaConsumida:ArrayList<Conversation>


    init {
        lista = ConversationDao()
    }

    fun devuelveLista(){
        try {
            viewModelScope.launch {
                listaConsumida =  withContext(Dispatchers.IO) {
                    lista.recuperarPublicaciones()
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


    fun mandarMensaje(mensaje:String, fecha:String){
        try {

            //var menssage=Message("11","12",mensaje, fecha)

            viewModelScope.launch {
             // peticionMensaje.insertarPublicacion(mensaje,fecha)
                listaConsumida = withContext(Dispatchers.IO){
                    lista.recuperarPublicaciones()
                }
                adaptador.value!!.update(listaConsumida)
            }


        }catch (ex:Exception){
            Log.e(ListConversationViewModel::class.simpleName.toString(), ex.message.toString())
        }



    }
}