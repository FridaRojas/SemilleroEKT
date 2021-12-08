package com.example.agileus.ui.login.iniciosesion


import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.config.InitialApplication.Companion.preferenciasGlobal
import com.example.agileus.webservices.dao.LoginDao
import com.example.agileus.models.LoginResponse
import com.example.agileus.models.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InicioSesionViewModel : ViewModel() {
    var list : LoginDao
    var inicioExitoso = MutableLiveData<Boolean>()

    val correo = preferenciasGlobal.recuperaNombre()
    val password = preferenciasGlobal.recuperaPassword()
    //shared
    //private var userList = mutableListOf<Users>()
    //var usuariosShared = MutableLiveData<List<Users>>()
    //private val repository = Repository()

    init {
        list = LoginDao()
    }


// recuperarToken
    fun recuperarLogueo(users: Users): List<LoginResponse>{
        //Log.i("mensaje", "ver")
        try {
            viewModelScope.launch {
                inicioExitoso.value = withContext(Dispatchers.IO){
                    list.iniciarSesion(users)
                }!!
            }

            //Log.d("status","$status")
        } catch (ex : Exception) {
            inicioExitoso.value = false
            //Log.e("Corroborar Login", ex.message.toString())
        }
        return emptyList()
    }
    fun cerrarSesion(view: View) {
        preferenciasGlobal.cerrarSesion()
    }

}