package com.example.agileus.webservices.dao

import android.util.Log
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.DataLogin
import com.example.agileus.models.DataPersons
import com.example.agileus.models.UserBossResponse
import com.example.agileus.models.UserLoginResponse
import com.example.agileus.ui.login.data.model.LoginResponse
import java.lang.Exception

class LoginDao {

    suspend fun getUserLogin(id: String) : UserLoginResponse {
        lateinit var data : Any
        lateinit var userLoginResponse: UserLoginResponse

        val callRespuesta = InitialApplication.LoginServiceGlobal.iniciarSesionLogin()
        var response = callRespuesta?.execute()

        try {
            if (response != null) {
                if (response.isSuccessful) {
                    var status = response.body()!!.status.toString()
                    var msj = response.body()!!.msj.toString()
                    if (response.body()!!.status.equals("ACCEPTED")) {
                        data = response.body()!!.data.toString() as DataLogin
                        userLoginResponse = UserLoginResponse(status, msj, data)
                    } else {
                        data = response.body()!!.data.toString() as String
                        userLoginResponse = UserLoginResponse(status, msj, data)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("error", e.toString())
        }
        return userLoginResponse
    }
}