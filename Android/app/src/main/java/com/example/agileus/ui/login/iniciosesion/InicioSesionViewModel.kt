package com.example.agileus.ui.login.iniciosesion


import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.TasksAdapter
import com.example.agileus.models.DataPersons
import com.example.agileus.models.DataTask
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
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

    companion object{
        var usersByBoss = false

    }

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

    /*
    fun getUsersByBoss() {
        var listUsers = ArrayList<DataPersons>()

        viewModelScope.launch {
            listUsers = withContext(Dispatchers.IO){
                //true "Si tiene hijos" -> alto
                list.getUsersByBoss("618d9c26beec342d91d747d6")

                //false "No tiene hijos" -> bajo
                //list.getUsersByBoss("61a83c49d036090b8e8db3c5")

            }
            if (listUsers != null) {
                usersByBoss = !listUsers.isEmpty()
            }else{
                usersByBoss = false
            }
        }
        if(listUsers.isEmpty()){
            usersByBoss = false
        }else{
            usersByBoss = true
        }
    }*/
    fun cerrarSesion(view: View) {
        preferenciasGlobal.cerrarSesion()
    }
}