package com.example.agileus.webservices.dao

import android.util.Log
import com.example.agileus.config.InitialApplication
import com.example.agileus.config.InitialApplication.Companion.preferenciasGlobal
import com.example.agileus.models.*
import retrofit2.Call
import com.example.agileus.models.LoginResponse
import com.example.agileus.models.Users
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.id
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.nombre
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.nombreRol
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.status
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.tokenAuth
import com.google.gson.internal.LinkedTreeMap
import retrofit2.Response

class LoginDao {
    val STATUS_ACCEPTED = "ACCEPTED"
    val STATUS_BAD_REQUEST = "BAD_REQUEST"


    fun iniciarSesion(usuario:Users): Boolean {

        var STATUS: Boolean = false
        //    val STATUS_BAD_REQUEST = "BAD_REQUEST"

        val callRespuesta = InitialApplication.LoginServiceGlobal.iniciarSesionLogin(usuario)
        var responseDos: Response<LoginResponse> = callRespuesta.execute()
        //var user:LoginResponse

        if (responseDos.isSuccessful) {
            //Log.d("body",responseDos.body().toString())
            //Log.d("body",responseDos.body()?.status.toString())
            //Log.d("body",responseDos.body().data.toString())

            if (responseDos.body() != null) {
                val almacenar: LoginResponse = responseDos.body()!!
                var guardarData:Data = Data()

                if (almacenar.status == "ACCEPTED") {
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

                    id=guardarData.id.toString()
                    tokenAuth=guardarData.tokenAuth.toString()
                    nombre=guardarData.nombre.toString()
                    nombreRol =guardarData.nombreRol.toString()


                    status  = true

                    preferenciasGlobal.guardarDatosInicioSesion(
                        mapa["id"].toString(),
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
                    status = false
                }
            }
            else {
                status = false
            }
        }
        else{
            status = false
        }
            //Log.d("status", status.toString())
            return status
    }

    suspend fun getUsersByBoss(id: String): ArrayList<Data> {
        var listaUsers = ArrayList<Data>()
        lateinit var usersListResponse: LoginResponse

        val callRespuesta = InitialApplication.webServiceGlobalNivel.getUsersByBoss(id)
        var response = callRespuesta?.execute()

        try {
            if (response != null) {
                Log.d("usuario", "${response.body()}")
                Log.d("usuario", "${response.errorBody()}")
                Log.d("usuario", "${response.code()}")


                if (response.isSuccessful) {
                    //usersList = response.body()!!
                        var status = response.body()!!.status
                    var msj = response.body()!!.msj
                    if (response.body()!!.status.equals("BAD_REQUEST")) {
                        //usersListResponse = UserBossResponse(status, msj, response.body()!!.data as String)
                        listaUsers =  emptyList<Data>() as ArrayList<Data>

                    } else {
                        usersListResponse = LoginResponse(status, msj, response.body()!!.data as ArrayList<Data>)
                        listaUsers = usersListResponse.data as ArrayList<Data>
                    }
                } else {
                    listaUsers = emptyList<Data>() as ArrayList<Data>
                }
            } else {
                listaUsers = emptyList<Data>() as ArrayList<Data>
            }
        } catch (e: Exception) {
            Log.e("error", e.toString())
        }
        return listaUsers
    }
}

