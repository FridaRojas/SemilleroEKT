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
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.lang.Exception


class LoginDao {
    val STATUS_ACCEPTED = "ACCEPTED"
    val STATUS_BAD_REQUEST = "BAD_REQUEST"


    fun iniciarSesion(usuario:Users): Boolean {

        var STATUS: Boolean = false
        //    val STATUS_BAD_REQUEST = "BAD_REQUEST"

        val callRespuesta = InitialApplication.LoginServiceGlobal.iniciarSesionLogin(usuario)
        var responseDos: Response<LoginResponse> = callRespuesta.execute()

        if (responseDos.isSuccessful) {
            if (responseDos.body() != null) {
                val almacenar: LoginResponse = responseDos.body()!!
                if (almacenar.status == "ACCEPTED") {
                    STATUS = true
//                    user = LoginResponse(almacenar.status, almacenar.msj, almacenar.data as Data)
                    idUser = almacenar.data.id.toString()
                    rol = almacenar.data.nombreRol.toString()
                    idnombre = almacenar.data.nombre.toString()
                    tokenAuth = almacenar.data.tokenAuth.toString()
                }
                if (almacenar.status == "BAD_REQUEST") {
                    //   user = LoginResponse(almacenar.status, almacenar.msj, almacenar.data as String)
                    STATUS = false
                    idUser = almacenar.data.id.toString()
                    rol = almacenar.data.nombreRol.toString()
                    idnombre = almacenar.data.nombre.toString()
                    idGrupo = almacenar.data.idgrupo.toString()
                    tokenAuth = almacenar.data.tokenAuth.toString()


                }
                if (almacenar.status == "BAD_REQUEST") {
                    //   user = LoginResponse(almacenar.status, almacenar.msj, almacenar.data as String)
                    STATUS = false

                }
                status = STATUS
            }
        }

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

