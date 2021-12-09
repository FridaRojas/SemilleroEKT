package com.example.agileus.webservices.dao

import com.example.agileus.config.InitialApplication
import com.example.agileus.models.Data
import com.example.agileus.models.LoginResponse
import com.example.agileus.models.Users
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.idGrupo
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.idUser
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.idnombre
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.rol
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.status
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.tokenAuth
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
                    user = LoginResponse(almacenar.status, almacenar.msj, almacenar.data as Data)
                    idUser = (almacenar.data as Data).id.toString()
                    rol= (almacenar.data as Data).nombreRol.toString()
                    idnombre= (almacenar.data as Data).nombre.toString()
                    idGrupo = (almacenar.data as Data).idgrupo.toString()
                    tokenAuth = (almacenar.data as Data).tokenAuth.toString()


                }
                if (almacenar.status =="BAD_REQUEST")
                {
                    user = LoginResponse(almacenar.status, almacenar.msj, almacenar.data as String)
                    STATUS=false

                }
                status=STATUS
            }
        }
    return STATUS
    }
}

