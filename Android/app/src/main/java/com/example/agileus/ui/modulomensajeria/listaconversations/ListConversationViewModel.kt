package com.example.agileus.ui.modulomensajeria.listaconversations

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.ChatsAdapter
import com.example.agileus.adapters.GroupsAdapter
import com.example.agileus.models.Chats
import com.example.agileus.models.Contacts
import com.example.agileus.models.Groups
import com.example.agileus.ui.modulomensajeria.listcontacts.ListContactsViewModel
import com.example.agileus.webservices.dao.MessageDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListConversationViewModel : ViewModel() {

    var adaptadorGrupos = MutableLiveData<GroupsAdapter>()
    var adaptadorChats = MutableLiveData<ChatsAdapter>()
    lateinit var lista : MessageDao
    lateinit var listaConsumidaGrupos:ArrayList<Groups>
    lateinit var listadeChats:ArrayList<Chats>
    var chatsdeUsuario = MutableLiveData<ArrayList<Chats>>()
    var pruebaArreglo = MutableLiveData<ArrayList<Chats>>()
    init {
        lista = MessageDao()
    }

    fun devuelveListaGrupos(idUser:String){
        try {
            viewModelScope.launch {
                listaConsumidaGrupos =  withContext(Dispatchers.IO) {
                    lista.recuperarListadeGrupos(idUser)
                }
                Log.i("mensaje", "${listaConsumidaGrupos.size}")
                if (listaConsumidaGrupos != null){
                    if(listaConsumidaGrupos.isNotEmpty()){
                        adaptadorGrupos.postValue(GroupsAdapter(listaConsumidaGrupos as ArrayList<Groups>))
                    }
                }
            }
        }catch (ex:Exception){
            Log.e(ListConversationViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }

    fun devuelveListaChats(idUser:String){
        try {
            viewModelScope.launch {
                listadeChats =  withContext(Dispatchers.IO) {
                    lista.recuperarListadeChats(idUser)
                }
                if (listadeChats != null){
                    if(listadeChats.isNotEmpty()){
                        adaptadorChats.postValue(ChatsAdapter(listadeChats as ArrayList<Chats>))
                        chatsdeUsuario.value = listadeChats!!
                        pruebaArreglo.value = listadeChats!!
                    }
                }
            }
        }catch (ex:Exception){
            Log.e(ListConversationViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }

    fun filtrarChats(idUser:String,listaAdaptada:ArrayList<Chats>){
        try {
            viewModelScope.launch {
                listadeChats =  withContext(Dispatchers.IO) {
                    lista.recuperarListadeChats(idUser)
                }
                if (listadeChats != null){
                    if(listadeChats.isNotEmpty()){
                        adaptadorChats.value!!.update(listaAdaptada)
                    }
                }
            }
        }catch (ex:Exception){
            Log.e(ListConversationViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }
}