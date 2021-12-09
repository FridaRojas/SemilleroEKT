package com.example.agileus.ui.login.data.dao

import android.util.Log
import com.example.agileus.config.InitialApplication
import com.example.agileus.ui.login.data.model.*
import com.example.agileus.ui.login.data.service.LoginApi
import com.example.agileus.ui.login.ui.login.InicioSesionFragment.Companion.Rol
import com.example.agileus.ui.login.ui.login.InicioSesionFragment.Companion.idUser
import com.example.agileus.ui.login.ui.login.InicioSesionFragment.Companion.status
import com.example.agileus.ui.login.ui.login.InicioSesionFragment.Companion.token
import com.example.agileus.ui.login.ui.login.InicioSesionFragment.Companion.tokenPush
import com.example.agileus.ui.login.ui.login.InicioSesionFragment.Companion.userName
import retrofit2.Call
import retrofit2.Response

class LoginDao {

    var STATUS: Boolean=false
//    val STATUS_BAD_REQUEST = "BAD_REQUEST"

    fun iniciarSesion(usuario: Users): Boolean {
        val callRespuesta = InitialApplication.LoginServiceGlobal.iniciarSesionLogin(usuario)
        var responseDos: Response<LoginResponse> = callRespuesta.execute()
        lateinit var user:LoginResponse
        if (responseDos.isSuccessful) {

            if (responseDos.body() != null) {
                val almacenar: LoginResponse = responseDos.body()!!
                //Log.d("almacenar", "${almacenar.data.id}")
                //idUser= almacenar.data.id.toString()
                //rol= almacenar.data.nombreRol.toString()
                if (almacenar.status == "ACCEPTED")
                {
                    STATUS=true
//                    user = LoginResponse(almacenar.status, almacenar.msj, almacenar.data as Data)
                    idUser = almacenar.data.id.toString()
                    token= almacenar.data.tokenAuth.toString()
                    tokenPush=almacenar.data.token.toString()
                    Rol=almacenar.data.nombreRol.toString()
                    userName=almacenar.data.nombre.toString()
                }
                if (almacenar.status =="BAD_REQUEST")
                {
                 //   user = LoginResponse(almacenar.status, almacenar.msj, almacenar.data as String)
                    STATUS=false

                }
                status=STATUS
            }
        }
    return STATUS
    }
}

