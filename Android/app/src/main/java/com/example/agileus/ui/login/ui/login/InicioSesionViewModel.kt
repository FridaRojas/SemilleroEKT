package com.example.agileus.ui.login.ui.login


import android.content.Intent
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.agileus.config.InitialApplication.Companion.preferenciasGlobal
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.login.data.dao.LoginDao
import com.example.agileus.ui.login.data.model.LoginResponse
import com.example.agileus.ui.login.data.model.Users
import com.example.agileus.ui.login.repository.Repository
import com.example.agileus.ui.login.ui.login.InicioSesionFragment.Companion.status
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
        //shared
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

            Log.d("status","$status")
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