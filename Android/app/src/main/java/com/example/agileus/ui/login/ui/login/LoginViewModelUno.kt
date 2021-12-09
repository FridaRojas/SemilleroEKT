package com.example.agileus.ui.login.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.agileus.ui.login.data.model.UserUno
import com.example.agileus.ui.login.repository.Repository

class LoginViewModelUno : ViewModel() {
    //guarda la infor del usuario
    private val user = MutableLiveData<List<UserUno>>()
    private var useList = mutableListOf<UserUno>()
    private val Repository = com.example.agileus.ui.login.repository.Repository()

    init {
        useList.add(UserUno(Repository.getUserName(), Repository.getPassword()!!))
        user.value = useList
    }

    fun signIn(username: String, pass: String):Boolean{
        return getUsername().equals(username) && getUserPassword().equals(pass)
    }

    fun addUser(username:String, pass:String) {
        val use:UserUno = UserUno(username, pass)
        Repository.addUser(use)
        useList.add(use)
        user.value = useList
    }

    fun getUsername()= useList[0].username
    fun  getUserPassword() = useList[0].pass

}

//devuelve nom de usuario y contrasela