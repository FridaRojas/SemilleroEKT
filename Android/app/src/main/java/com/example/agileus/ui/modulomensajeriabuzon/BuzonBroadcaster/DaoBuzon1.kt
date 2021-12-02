package com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster

import android.util.Log
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.BuzonResp
import com.example.agileus.models.ListaUsers
import com.example.agileus.models.MensajeBodyBroadcaster
import com.example.agileus.models.MsgBodyUser
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listaBrd
import com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster.BuzonDetallesViewModel.Companion.listaus
import retrofit2.Response

class DaoBuzon1 {

    suspend fun recuperarListadeContactos(idUser: String): ArrayList<ListaUsers> {
        listaus = ArrayList()
        try {
            Log.i("hola", "paso2")
            val callRespuesta = InitialApplication.BroadcastServiceGlobalTasks2.getList(idUser)
            var ResponseDos: Response<ArrayList<ListaUsers>> = callRespuesta.execute()
              if (ResponseDos.isSuccessful) {
                Log.i("hola", "paso3")
                listaus = ResponseDos.body()!!
                listaus.forEach {
                    Log.d("name", it.nombre)
                }
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
            Log.i("hola", "paso2")
            val callRespuesta = InitialApplication.BroadcastServiceGlobalTasks2.getbuzon(idUser)
            var ResponseDos: Response<ArrayList<BuzonResp>> = callRespuesta.execute()
            if (ResponseDos.isSuccessful) {

                Log.i("hola", "paso3")

                listaBrd = ResponseDos.body()!!
                listaBrd.forEach {
                    Log.d("asu", it.Asunto.toString())
                }
            } else {
                Log.i("ERROR", ResponseDos.code().toString())
            }

        } catch (ex: Exception) {
            Log.i("ERROR", "${ex.toString()}")
        }
        return listaBrd
    }

    suspend fun recuperarMensajesBrd1 (idUser: String): ArrayList<BuzonResp> {
        listaBrd = ArrayList()
        try {
            Log.i("hola", "paso2")
            val callRespuesta = InitialApplication.BroadcastServiceGlobalTasks2.getmybuzon(idUser)
            var ResponseDos: Response<ArrayList<BuzonResp>> = callRespuesta.execute()
            if (ResponseDos.isSuccessful) {

                Log.i("hola", "paso3")

                listaBrd = ResponseDos.body()!!
                listaBrd.forEach {
                    Log.d("asu", it.Asunto.toString())
                }
            } else {
                Log.i("ERROR", ResponseDos.code().toString())
            }

        } catch (ex: Exception) {
            Log.i("ERROR", "${ex.toString()}")
        }
        return listaBrd
    }


    suspend fun getcustompost(post: MensajeBodyBroadcaster):Response<MensajeBodyBroadcaster> {
        return InitialApplication.BroadcastServiceGlobalTasks2.pushpost(post)
    }
    suspend fun getcustompush(post: MsgBodyUser):Response<MsgBodyUser> {
        return InitialApplication.BroadcastServiceGlobalTasks2.pushrequest(post)
    }



}
