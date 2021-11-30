package com.example.agileus.ui.login.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.ui.login.data.model.Data
import com.example.agileus.ui.login.ui.login.services.GetUserDao

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel :  ViewModel() {

    var lista: LoginProvider
    lateinit var listafiltrada: ArrayList<Data>
    lateinit var listaConsumida: ArrayList<Data>

    init {
        lista = LoginProvider()
    }


    fun devuelveUser() {
        Log.d("paso","a")
        listaConsumida = ArrayList()
        try {
            viewModelScope.launch {
                listaConsumida = withContext(Dispatchers.IO) {
                    lista.recupera(listaConsumida) ///recuperarListaUsuario
                }
                Log.d("Data","hola")
                if (listaConsumida.isNotEmpty()) {
                    listaConsumida.forEach {

                        Log.d("Data","${it.nombre}")
                    }

                }
            }
        }
        catch (ex:Exception){
            Log.e("Exception", ex.message.toString())
        }
         }
    }


