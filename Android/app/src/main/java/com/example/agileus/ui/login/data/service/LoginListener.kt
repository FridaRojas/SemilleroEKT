package com.example.agileus.ui.login.data.service

import com.example.agileus.ui.login.data.model.LoginResponse

interface LoginListener {
    fun onLoginSuccess( loginResponse: LoginResponse )
    fun onLoginFail( error : String )
}