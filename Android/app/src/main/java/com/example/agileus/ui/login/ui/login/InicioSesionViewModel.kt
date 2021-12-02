package com.example.agileus.ui.login.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.ui.login.data.dao.LoginDao
import com.example.agileus.ui.login.data.model.LoginResponse
import com.example.agileus.ui.login.data.model.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InicioSesionViewModel : ViewModel() {
    var list : LoginDao
    var inicioExitoso = MutableLiveData<Boolean>()

    init {
        list = LoginDao()
    }

    fun recuperarLogueo(users: Users): List<LoginResponse>{
        Log.i("mensaje", "ver")
        try {
            viewModelScope.launch {
                inicioExitoso.value = withContext(Dispatchers.IO){
                    list.iniciarSesion(users)
                }!!
            }
        } catch (ex : Exception) {
            inicioExitoso.value = false
            Log.e("Corroborar Login", ex.message.toString())
        }
        return emptyList()
    }
}