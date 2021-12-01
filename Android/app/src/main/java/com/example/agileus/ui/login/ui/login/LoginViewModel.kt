package com.example.agileus.ui.login.ui.login

import android.util.Log
import android.widget.Button
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.R
import com.example.agileus.ui.login.data.model.User
import com.example.agileus.ui.login.data.dao.LoginDao

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {
    var lista: LoginDao
    lateinit var listaConsumida: ArrayList<User>


    init {
        lista = LoginDao()
    }



    fun devuelveUser(/* pasar parametros login */) {
        listaConsumida = ArrayList()
        try {
            viewModelScope.launch {
                listaConsumida = withContext(Dispatchers.IO) {
                    lista.recuperarUser(listaConsumida) ///recuperarListaUsuario
                }

                if (listaConsumida.isNotEmpty()) {
                    listaConsumida.forEach(){
                        Log.i("vista","${it.data.nombre}")
                    }
                    /*
                    * logica del login dado que ya se deberia tener datos de registro junto con la lista de usuarios
                    * */
//                    listaConsumida[0].data.
                    ////trabajar con lo obtenido
                }
            }
        }catch (ex: Exception) {
            Log.e("aqui", ex.message.toString())
        }
    }
}