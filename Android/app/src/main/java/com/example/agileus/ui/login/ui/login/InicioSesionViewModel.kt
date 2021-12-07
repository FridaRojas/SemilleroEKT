package com.example.agileus.ui.login.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.TasksAdapter
import com.example.agileus.models.DataPersons
import com.example.agileus.models.DataTask
import com.example.agileus.ui.login.data.dao.LoginDao
import com.example.agileus.ui.login.data.model.LoginResponse
import com.example.agileus.ui.login.data.model.Users
import com.example.agileus.ui.modulotareas.listatareas.TaskViewModel
import com.example.agileus.ui.modulotareas.listenerstareas.TaskListListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InicioSesionViewModel : ViewModel() {
    var list : LoginDao
    var inicioExitoso = MutableLiveData<Boolean>()

    companion object{
        var usersByBoss = false

    }

    init {
        list = LoginDao()
    }

    fun recuperarLogueo(users: Users): List<LoginResponse>{
        //Log.i("mensaje", "ver")
        try {
            viewModelScope.launch {
                inicioExitoso.value = withContext(Dispatchers.IO){
                    list.iniciarSesion(users)
                }!!
            }
        } catch (ex : Exception) {
            inicioExitoso.value = false
            //Log.e("Corroborar Login", ex.message.toString())
        }
        return emptyList()
    }

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
    }
}