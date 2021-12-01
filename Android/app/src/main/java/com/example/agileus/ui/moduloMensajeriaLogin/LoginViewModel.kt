package com.example.agileus.ui.moduloMensajeriaLogin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.models.User
import com.example.agileus.webservices.dao.LoginDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {
    var lista: LoginDao
    lateinit var listafiltrada: ArrayList<User>
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
                    * aqui va lo logica del login dado que ya se deberia tener datos de registro junto con la lista de usuarios
                    * */
//                    listaConsumida[0].data.
                    ////aqui se trabaja con lo obtenido
                }
            }
        }catch (ex: Exception) {
            Log.e("aqui", ex.message.toString())
        }
    }
}