package com.example.agileus.webservices.dao

import android.util.Log
import com.example.agileus.config.InitialApplication
import com.example.agileus.config.InitialApplication.Companion.preferenciasGlobal
import com.example.agileus.models.*
import retrofit2.Call
import com.example.agileus.models.LoginResponse
import com.example.agileus.models.Users
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.status
import com.google.gson.internal.LinkedTreeMap
import retrofit2.Response

class LoginDao {
    val STATUS_ACCEPTED = "ACCEPTED"
    val STATUS_BAD_REQUEST = "BAD_REQUEST"


    fun iniciarSesion(usuario:Users): Boolean {

        var STATUS: Boolean = false

        val callRespuesta = InitialApplication.LoginServiceGlobal.iniciarSesionLogin(usuario)
        var responseDos: Response<LoginResponse> = callRespuesta.execute()
        //var user:LoginResponse

        if (responseDos.isSuccessful) {
            Log.d("body",responseDos.body().toString())
            Log.d("body",responseDos.body()?.status.toString())
            //Log.d("body",responseDos.body().data.toString())

            if (responseDos.body() != null) {
                val almacenar: LoginResponse = responseDos.body()!!
                var guardarData:Data = Data()

                if (almacenar.status == "ACCEPTED") {
                    STATUS = true
                    var mapa:LinkedTreeMap<String,Any?> = responseDos.body()!!.data as LinkedTreeMap<String, Any?>
                    guardarData.id = mapa["id"].toString()
                    guardarData.idUser = mapa["idUser"].toString()
                    guardarData.correo = mapa["correo"].toString()
                    guardarData.fechaInicio = mapa["fechaInicio"].toString()
                    guardarData.fechaTermino = mapa["fechaTermino"].toString()
                    guardarData.numeroEmpleado = mapa["numeroEmpleo"].toString()
                    guardarData.nombre = mapa["nombre"].toString()
                    guardarData.password = mapa["password"].toString()
                    guardarData.nombreRol = mapa["nombreRol"].toString()
                    guardarData.opcionales = mapa["opcionales"].toString()
                    guardarData.token = mapa["token"].toString()
                    guardarData.telefono = mapa["mapa"].toString()
                    guardarData.statusActivo = mapa["statusActivo"].toString()
                    guardarData.curp = mapa["curp"].toString()
                    guardarData.rfc = mapa["rfc"].toString()
                    guardarData.idgrupo = mapa["idgrupo"].toString()
                    guardarData.idsuperiorInmediato = mapa["idsuperiorInmediato"].toString()
                    guardarData.tokenAuth = mapa["tokenAuth"].toString()

                    almacenar.data = guardarData

                    preferenciasGlobal.guardarDatosInicioSesion(
                        mapa["idUser"].toString(),
                        mapa["correo"].toString(),
                        mapa["numeroEmpleo"].toString(),
                        mapa["nombre"].toString(),
                        mapa["nombreRol"].toString(),
                        mapa["mapa"].toString(),
                        mapa["curp"].toString(),
                        mapa["rfc"].toString(),
                        mapa["tokenAuth"].toString(),
                        mapa["idgrupo"].toString(),
                        mapa["idsuperiorInmediato"].toString(),
                        true
                    )

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

