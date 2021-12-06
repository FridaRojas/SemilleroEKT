package com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster

import android.util.Log
import com.example.agileus.adapters.BuzonAdapterResponse
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.*
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listaBrd
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listaus
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.ReceiverBuzonBroadcastViewModel.Companion.listaBrd1
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.ReceiverBuzonBroadcastViewModel.Companion.mensajes
import retrofit2.Response

class DaoBuzon1 {


    suspend fun recuperarListadeContactos(idUser: String): ArrayList<Contacts> {
        listaus = ArrayList()
        try {
            val callRespuesta = InitialApplication.BroadcastServiceGlobalTasks2.getList(idUser)
            val ResponseDos: Response<ArrayList<Contacts>> = callRespuesta.execute()
              if (ResponseDos.isSuccessful) {
                listaus = ResponseDos.body()!!
            } else {
                Log.i("ERROR", ResponseDos.code().toString())
            }

        } catch (ex: Exception) {
            Log.i("ERROR", "${ex.toString()}")
        }
        return listaus
    }

    suspend fun recuperarMensajesBrd (idUser: String): ArrayList<BuzonResp> {
        listaBrd = ArrayList()
        try {
            val callRespuesta = InitialApplication.BroadcastServiceGlobalTasks2.getbuzon(idUser)
            val ResponseDos: Response<ArrayList<BuzonResp>> = callRespuesta.execute()
            if (ResponseDos.isSuccessful) {
                listaBrd = ResponseDos.body()!!
            } else {
                Log.i("ERROR", ResponseDos.code().toString())
            }

        } catch (ex: Exception) {
            Log.i("ERROR", "${ex.toString()}")
        }
        return listaBrd
    }

    suspend fun recuperarMensajesBrd1 (idUser: String): ArrayList<Chats> {
        listaBrd1 = ArrayList()
        try {
            val callRespuesta = InitialApplication.BroadcastServiceGlobalTasks2.getmybuzon(idUser)
            var ResponseDos: Response<ArrayList<Chats>> = callRespuesta.execute()
            if (ResponseDos.isSuccessful) {
                 listaBrd1 = ResponseDos.body()!!
            } else {
                Log.i("ERROR", ResponseDos.code().toString())
            }

        } catch (ex: Exception) {
            Log.i("ERROR", "${ex.toString()}")
        }

        return listaBrd1
    }

    suspend fun recuperarEnviadosBrd (idUser: String): ArrayList<BuzonComunicados> {
        mensajes = ArrayList()
        try {
            val callRespuesta = InitialApplication.BroadcastServiceGlobalTasks2.getenviados(idUser)
            var ResponseDos: Response<ArrayList<BuzonComunicados>> = callRespuesta.execute()
            if (ResponseDos.isSuccessful) {
                mensajes = ResponseDos.body()!!

            } else {
                Log.i("ERROR", ResponseDos.code().toString())
            }

        } catch (ex: Exception) {
            Log.i("ERROR", "${ex.toString()}")
        }

        return mensajes
    }



    suspend fun getcustompost(post: MensajeBodyBroadcaster):Response<MensajeBodyBroadcaster> {
        return InitialApplication.BroadcastServiceGlobalTasks2.pushpost(post)
    }
    suspend fun getcustompush(post: MsgBodyUser):Response<MsgBodyUser> {
        return InitialApplication.BroadcastServiceGlobalTasks2.pushrequest(post)
    }



}
