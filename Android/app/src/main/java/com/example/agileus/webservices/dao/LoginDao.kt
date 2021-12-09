package com.example.agileus.webservices.dao

import com.example.agileus.config.InitialApplication
import com.example.agileus.models.*
import retrofit2.Call
import com.example.agileus.models.LoginResponse
import com.example.agileus.models.Users
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.idGrupo
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.idUser
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.idnombre
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.rol
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.status
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.tokenAuth
import com.google.gson.internal.LinkedTreeMap
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.Exception


class LoginDao {
    val STATUS_ACCEPTED = "ACCEPTED"
    val STATUS_BAD_REQUEST = "BAD_REQUEST"


    fun iniciarSesion(usuario:Users): Boolean {

        var STATUS: Boolean = false

        val callRespuesta = InitialApplication.LoginServiceGlobal.iniciarSesionLogin(usuario)
        var responseDos: Response<LoginResponse> = callRespuesta.execute()
        //var user:LoginResponse

        if (responseDos.isSuccessful) {
            if (responseDos.body() != null) {
                val almacenar: LoginResponse = responseDos.body()!!
                var guardarData:Data = Data()

                if (almacenar.status == "ACCEPTED") {
                    STATUS = true
                    var mapa:LinkedTreeMap<String,Any?> = responseDos.body()!!.data as LinkedTreeMap<String, Any?>
                    guardarData.id = mapa["id"].toString()
                    guardarData.idUser = mapa["idUser"].toString()

                    almacenar.data = guardarData


                }

                else {
                    STATUS = false

                }
            }
            else {
                STATUS = false

            }
        }
        else{
            STATUS = false
        }

            status = STATUS

            return STATUS

    }
                /*
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
    }*/
}

