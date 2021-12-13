package com.example.agileus.ui.login.iniciosesion


import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.TasksAdapter
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.agileus.config.InitialApplication.Companion.preferenciasGlobal
import com.example.agileus.models.*
import com.example.agileus.webservices.dao.LoginDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InicioSesionViewModel : ViewModel() {

    companion object{
        var userBoss = ""
    }


    var list : LoginDao
    var inicioExitoso = MutableLiveData<Boolean>()
    var userByBossId = MutableLiveData<String>()
    var listUsers = ArrayList<Data>()

    //shared
    //private var userList = mutableListOf<Users>()
    //var usuariosShared = MutableLiveData<List<Users>>()
    //private val repository = Repository()

    init {
        list = LoginDao()
        userByBossId.value = "bajo"
        //getUsersByBoss()
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



    fun getUsersByBoss() {
        try{
            viewModelScope.launch {
                listUsers = withContext(Dispatchers.IO){
                    list.getUsersByBoss(preferenciasGlobal.recuperarIdSesion())
                }
                if (listUsers.isNullOrEmpty()) {
                    Log.d("usuarios", "${listUsers.size}")
                    if(listUsers.isNotEmpty()){
                        userByBossId.value = "alto"
                    }
                }else{
                    userByBossId.value = "bajo"
                }
                userBoss = userByBossId.value.toString()

                Log.d("usuario", "Hijos: ${userByBossId.value}")
            }

        }catch (ex : Exception) {
            Log.e("usuario", ex.message.toString())
        }
    }

}