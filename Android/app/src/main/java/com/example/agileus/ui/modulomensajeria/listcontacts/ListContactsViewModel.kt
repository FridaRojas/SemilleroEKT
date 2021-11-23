package com.example.agileus.ui.modulomensajeria.listcontacts

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.ContactsAdapter
import com.example.agileus.models.Contacts
import com.example.agileus.ui.modulomensajeria.listaconversations.ListConversationViewModel
import com.example.agileus.webservices.dao.ConversationDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListContactsViewModel : ViewModel() {

    var adaptador = MutableLiveData<ContactsAdapter>()
    lateinit var lista : ConversationDao
    lateinit var listaConsumida:ArrayList<Contacts>

    init {
        lista = ConversationDao()
    }

    fun devuelveLista(){
        try {
            viewModelScope.launch {
                listaConsumida =  withContext(Dispatchers.IO) {
                    lista.recuperarListadeContactos()
                }
                Log.i("mensaje", "${listaConsumida.size}")
                if (listaConsumida != null){
                    if(listaConsumida.isNotEmpty()){
                      //  adaptador.value = ContactsAdapter(listaConsumida as ArrayList<Contacts>)
                        adaptador.postValue(ContactsAdapter(listaConsumida as ArrayList<Contacts>))
                    }
                }

            }


        }catch (ex:Exception){
            Log.e(ListContactsViewModel::class.simpleName.toString(), ex.message.toString())
        }


    }



}