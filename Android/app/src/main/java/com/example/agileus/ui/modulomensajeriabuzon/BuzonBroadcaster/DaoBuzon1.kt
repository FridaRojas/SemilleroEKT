package com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster

import android.util.Log
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.*
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.idUser
import com.example.agileus.ui.login.iniciosesion.InicioSesionFragment.Companion.tokenAuth
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listaBrd
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listafiltrada
//import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listaBrd1
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listaus
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.ReceiverBuzonBroadcastViewModel.Companion.memsajes2
//import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.ReceiverBuzonBroadcastViewModel.Companion.listaBrd1
import com.example.agileus.utils.Constantes.broadlist
import retrofit2.Response

class DaoBuzon1() {

//    lateinit var listaBrd1: List<Chats1>

    suspend fun recuperarListadeContactos(idUser: String): ArrayList<Datos> {
        listaus = ArrayList()
        Log.d("tokenAuth", tokenAuth)
        try {
            val callRespuesta =
                InitialApplication.BroadcastServiceGlobalTasks2.getList(idUser, tokenAuth)
            val ResponseDos: Response<Contacts1> = callRespuesta.execute()
            if (ResponseDos.isSuccessful) {
                val x = ResponseDos.body()!!
//                  x.data
                Log.d("resp", ResponseDos.body()!!.mensaje.toString())
                Log.d("resp", ResponseDos.code().toString())
                Log.d("resp", x.data.size.toString())

                x.data.forEach {
                    Log.d("usuarios", it.nombre)
                    listaus.add(it)
                }


//                  listaus.forEach {
                //                    Log.d("resp",it.nombre)
                //              }
            } else {
                Log.i("ERROR", ResponseDos.code().toString())
            }

        } catch (ex: Exception) {
            Log.i("ERROR", "${ex.toString()}")
        }
        return listaus
    }


    suspend fun recuperarMensajesBrd(idUser: String): ArrayList<DataBuzon> {
        listaBrd = ArrayList()
        try {
            val callRespuesta =
                InitialApplication.BroadcastServiceGlobalTasks2.getbuzon(idUser, tokenAuth)
            val ResponseDos: Response<BuzonResp1> = callRespuesta.execute()
            if (ResponseDos.isSuccessful) {
                var x = ResponseDos.body()!!
                x.data.forEach {
                    listaBrd.add(it)
                }
            } else {
                Log.i("ERROR", ResponseDos.code().toString())
            }

        } catch (ex: Exception) {
            Log.i("ERROR", "${ex.toString()}")
        }
        return listaBrd
    }

    suspend fun recuperarMensajesBrd1(idUser: String): ArrayList<String> {

        try {
            val callRespuesta = InitialApplication.BroadcastServiceGlobalTasks2.getmybuzon(
                idUser,
                tokenAuth
            )
            var ResponseDos: Response<Chats1> = callRespuesta.execute()
            if (ResponseDos.isSuccessful) {
                var x = ResponseDos.body()!!
                x.data.forEach {
                    listafiltrada.add(it.idConversacion)
                }
            } else {
                Log.i("ERROR", ResponseDos.code().toString())
            }
        } catch (ex: Exception) {
            Log.i("ERROR", "${ex.toString()}")
        }

        return listafiltrada
    }

    suspend fun recuperarEnviadosBrd(idUser: String, sala: String) {

//        memsajes2 = ArrayList()
        try {
            Log.d("id recibido", idUser)
            val callRespuesta =
                InitialApplication.BroadcastServiceGlobalTasks2.getenviados(idUser, sala, tokenAuth)
            var ResponseDos: Response<BuzonComunicados> = callRespuesta.execute()
            if (ResponseDos.isSuccessful) {
                var mensajes = ResponseDos.body()!!
                mensajes.data.forEach {
                    memsajes2.add(it)
                }
            } else {
                Log.i("ERROR", ResponseDos.code().toString())
            }

        } catch (ex: Exception) {
            Log.i("ERROR", "${ex.toString()}")
        }
    }


    suspend fun getcustompost(post: MensajeBodyBroadcaster): Response<MensajeBodyBroadcaster> {
        return InitialApplication.BroadcastServiceGlobalTasks2.pushpost(post, tokenAuth)
    }

    suspend fun getcustompush(post: MsgBodyUser): Response<MsgBodyUser> {
        return InitialApplication.BroadcastServiceGlobalTasks2.pushrequest(idUser,post, tokenAuth)
    }

}

