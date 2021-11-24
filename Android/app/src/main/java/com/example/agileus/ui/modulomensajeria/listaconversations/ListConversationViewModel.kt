package com.example.agileus.ui.modulomensajeria.listaconversations

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.ContactsAdapter
import com.example.agileus.adapters.GroupsAdapter
import com.example.agileus.models.Contacts
import com.example.agileus.models.Groups
import com.example.agileus.ui.modulomensajeria.listcontacts.ListContactsViewModel
import com.example.agileus.webservices.dao.ConversationDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListConversationViewModel : ViewModel() {

    var adaptador = MutableLiveData<GroupsAdapter>()
    lateinit var lista : ConversationDao
    lateinit var listaConsumida:ArrayList<Groups>

    init {
        lista = ConversationDao()
    }

    fun devuelveLista(){
        try {
            viewModelScope.launch {
                listaConsumida =  withContext(Dispatchers.IO) {
                    lista.recuperarListadeGrupos()
                }
                Log.i("mensaje", "${listaConsumida.size}")
                if (listaConsumida != null){
                    if(listaConsumida.isNotEmpty()){
                        //  adaptador.value = ContactsAdapter(listaConsumida as ArrayList<Contacts>)
                        adaptador.postValue(GroupsAdapter(listaConsumida as ArrayList<Groups>))
                    }
                }

            }


        }catch (ex:Exception){
            Log.e(ListContactsViewModel::class.simpleName.toString(), ex.message.toString())
        }


    }



}