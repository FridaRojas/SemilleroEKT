package com.example.agileus.ui.modulomensajeria.listcontacts

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.ChatsAdapter
import com.example.agileus.adapters.ContactsAdapter
import com.example.agileus.models.Chats
import com.example.agileus.models.Contacts
import com.example.agileus.ui.modulomensajeria.listaconversations.ListConversationViewModel
import com.example.agileus.utils.Constantes
import com.example.agileus.webservices.dao.MessageDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListContactsViewModel : ViewModel() {

    var adaptador = MutableLiveData<ContactsAdapter>()
    lateinit var lista : MessageDao
    lateinit var listaConsumida:ArrayList<Contacts>
    var contactos = MutableLiveData<ArrayList<Contacts>>()



    init {
        lista = MessageDao()
    }

    fun devuelveLista(idUser:String){
        try {
            viewModelScope.launch {
                listaConsumida =  withContext(Dispatchers.IO) {
                    lista.recuperarListadeContactos(idUser)
                }
                Log.i("mensaje", "${listaConsumida.size}")
                if (listaConsumida != null){
                    if(listaConsumida.isNotEmpty()){
                        adaptador.postValue(ContactsAdapter(listaConsumida as ArrayList<Contacts>))
                        contactos.value = listaConsumida
                    }
                }
            }
        }catch (ex:Exception){
            Log.e(ListContactsViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }

    fun filtrarContactos(idUser:String,listaAdaptada:List<Contacts>){
        try {
            viewModelScope.launch {
                listaConsumida =  withContext(Dispatchers.IO) {
                    lista.recuperarListadeContactos(idUser)
                }
                if (listaConsumida != null){
                    if(listaConsumida.isNotEmpty()){
                        adaptador.value!!.update(listaAdaptada)
                    }else{

                    }
                }else{

                }
            }
        }catch (ex:Exception){
            Log.e(ListContactsViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }



}