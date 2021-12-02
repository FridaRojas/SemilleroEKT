package com.example.agileus.ui.login.data.dao

import android.util.Log
import com.example.agileus.config.InitialApplication
import com.example.agileus.ui.login.data.model.Data
import com.example.agileus.ui.login.data.model.LoginResponse
import com.example.agileus.ui.login.data.model.Users
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


class LoginDao {
    val STATUS_ACCEPTED = "ACCEPTED"
    val STATUS_BAD_REQUEST = "BAD_REQUEST"
    fun iniciarSesion(usuario:Users): Boolean {

        val callRespuesta = InitialApplication.LoginServiceGlobal.iniciarSesionLogin(usuario)
        var responseDos: Response<LoginResponse> = callRespuesta.execute()

       if (responseDos.isSuccessful){

           if (responseDos.body() != null){
                val almacenar:LoginResponse = responseDos.body()!!
             //  Log.i("almacenar", "${almacenar.msj}")
               return almacenar.status.equals(STATUS_ACCEPTED)
           } else{
               return false
           }


       }
        else {
            return false
       }


    }

}


