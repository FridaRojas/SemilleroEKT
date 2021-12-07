package com.example.agileus.ui.login.data.dao

import android.util.Log
import com.example.agileus.config.InitialApplication
import com.example.agileus.ui.login.data.model.*
import com.example.agileus.ui.login.data.service.LoginApi
import com.example.agileus.ui.login.ui.login.InicioSesionFragment.Companion.status
import retrofit2.Call
import retrofit2.Response

class LoginDao {

    var STATUS: Boolean=false
//    val STATUS_BAD_REQUEST = "BAD_REQUEST"

    fun iniciarSesion(usuario: Users): Boolean {
        val callRespuesta = InitialApplication.LoginServiceGlobal.iniciarSesionLogin(usuario)
        val responseDos: Response<LoginResponse> = callRespuesta.execute()

        if (responseDos.isSuccessful) {

            if (responseDos.body() != null) {
                val almacenar: LoginResponse = responseDos.body()!!
                Log.d("almacenar", "${almacenar.status}")

                if (almacenar.status == "ACCEPTED")
                    STATUS=true
                if (almacenar.status =="BAD_REQUEST")
                    STATUS=false
                status=STATUS
            }
        }
    return STATUS
    }
}

