package com.example.agileus.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.models.User
import com.example.agileus.webservices.dao.UserDaoLogin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {
    var lista: UserDaoLogin
    //private lateinit var listafiltrada: ArrayList<User>
    private lateinit var listaConsumida: ArrayList<User>
    private var usuarios = MutableLiveData<ArrayList<User>>()

    init {
        lista = UserDaoLogin()
    }


    fun devuelveUser() {
        listaConsumida = ArrayList()
        try {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    //lista.recuperarUserLogin(listaConsumida) ///recuperarListaUsuario
                }

                //if (listaConsumida != null){
                if (listaConsumida.isNotEmpty()) {
                    usuarios.value = listaConsumida

                    ////aqui se trabaja con lo obtenido
                }
            }
        }catch (excep:Exception){
            Log.e("message", excep.message.toString())
            //Log.e(LoginViewModel::class.simpleName.toString(), excep.message.toString())
        }

    }

}