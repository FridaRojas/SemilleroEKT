package com.example.agileus.ui.login.data.dao

import android.util.Log
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.*
import com.example.agileus.ui.login.data.model.Data
import com.example.agileus.ui.login.data.model.LoginResponse
import com.example.agileus.ui.login.data.model.Users
import com.example.agileus.ui.login.data.model.*
import com.example.agileus.ui.login.data.service.LoginApi
import com.example.agileus.ui.login.data.service.LoginListener
import com.example.agileus.ui.login.ui.login.InicioSesionFragment.Companion.idUser
import com.example.agileus.ui.login.ui.login.InicioSesionFragment.Companion.idnombre
import com.example.agileus.ui.login.ui.login.InicioSesionFragment.Companion.rol
import com.example.agileus.ui.login.ui.login.InicioSesionFragment.Companion.status
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.Exception


class LoginDao {
    val STATUS_ACCEPTED = "ACCEPTED"
    val STATUS_BAD_REQUEST = "BAD_REQUEST"

    lateinit var mLoginResponse: LoginResponse


    fun iniciarSesion(usuario:Users, listener:LoginListener) {

        val callRespuesta = InitialApplication.LoginServiceGlobal.iniciarSesionLogin(usuario)


            try {
                callRespuesta.enqueue(object: Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("mensaje", response.body().toString())
                            listener.onLoginSuccess(response.body()!!)
                        }else{
                            Log.d("mensaje", "no succesful")
                        }

                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.d("mensaje", t.message.toString())
                        Log.d("mensaje", t.stackTraceToString())
                        listener.onLoginFail(t.message.toString())
                    }

                })
                /*
                lateinit var data : Any
                if (response != null) {
                    if (response.isSuccessful) {
                        //usersList = response.body()!!
                        var status = response.body()!!.status
                        var msj = response.body()!!.msj
                        if (response.body()!!.status.equals(STATUS_ACCEPTED)) {
                            data = response.body()!!.data as DataLogin
                            mLoginResponse = LoginResponse(status, msj, data)

                        } else {
                            data = response.body()!!.data as String
                            mLoginResponse = LoginResponse(status, msj, data)
                        }
                    } else{
                        Log.e("mensajeError", "No successful")
                        mLoginResponse = LoginResponse(STATUS_BAD_REQUEST, "Es null1", null)

                    }
                } else {
                    Log.e("mensajeError", "Null")
                    mLoginResponse = LoginResponse(STATUS_BAD_REQUEST, "Es null2", null)

                }
                * */
            } catch (e: Exception) {
                Log.e("error", e.toString())
                mLoginResponse = LoginResponse(STATUS_BAD_REQUEST, "Es null3", null)

            }


    }

    suspend fun getUsersByBoss(id: String): ArrayList<DataPersons> {
        var listaUsers = ArrayList<DataPersons>()
        lateinit var usersListResponse: UserBossResponse

        val callRespuesta = InitialApplication.LoginServiceGlobal.getUsersByBoss(id)
        var response = callRespuesta?.execute()

        try {
            if (response != null) {
                if (response.isSuccessful) {
                    //usersList = response.body()!!
                        var status = response.body()!!.status
                    var msj = response.body()!!.msj
                    if (response.body()!!.status.equals("NOT_FOUND")) {
                        usersListResponse = UserBossResponse(status, msj, response.body()!!.data as String)
                        listaUsers =  emptyList<DataPersons>() as ArrayList<DataPersons>

                    } else {
                        usersListResponse = UserBossResponse(status, msj, response.body()!!.data as ArrayList<DataPersons>)
                        listaUsers = usersListResponse.data as ArrayList<DataPersons>
                    }
                } else {
                    listaUsers = emptyList<DataPersons>() as ArrayList<DataPersons>
                }
            } else {
                listaUsers = emptyList<DataPersons>() as ArrayList<DataPersons>
            }
        } catch (e: Exception) {
            Log.e("error", e.toString())
        }
        return listaUsers
    }
}

