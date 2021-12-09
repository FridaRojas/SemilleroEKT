package com.example.agileus.ui.modulologin

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.TasksAdapter
import com.example.agileus.models.DataLogin
import com.example.agileus.models.DataTask
import com.example.agileus.models.UserLoginResponse
import com.example.agileus.ui.modulotareas.listatareas.TaskViewModel
import com.example.agileus.ui.modulotareas.listenerstareas.TaskListListener
import com.example.agileus.webservices.dao.LoginDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {

    lateinit var usuarios: MutableLiveData<UserLoginResponse>
    var usuarioSesion : DataLogin = DataLogin()
    var userDao : LoginDao = LoginDao()

    fun devolverUsuarioLogin(listener: TaskListListener){
        viewModelScope.launch {
            usuarioSesion = withContext(Dispatchers.IO){
                userDao.getUserLogin("1")
            }

            usuarios.value= usuarioSesion as UserLoginResponse

        }
    }

}