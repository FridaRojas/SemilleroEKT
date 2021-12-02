package com.example.agileus.ui.modulomensajeriabuzon.BuzonBroadcaster

import android.util.Log
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.ListaUsers
import com.example.agileus.models.MensajeBodyBroadcaster
import com.example.agileus.models.MsgBodyUser
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
                    Log.d("name", it.nombre.toString())
                }
            } else {
                Log.i("ERROR", ResponseDos.code().toString())
            }

        } catch (ex: Exception) {
            Log.i("ERROR", "${ex.toString()}")
        }
        return listaus
    }

    suspend  fun pushPost(post: MensajeBodyBroadcaster)
    {

//        var callRespuesta =getcustompost(post)
//        var ResponseDos: Response<MensajeBodyBroadcaster> = callRespuesta.execute()

  //      var respuestas = ResponseDos.body()!!
    }

    suspend fun getcustompost(post: MensajeBodyBroadcaster):Response<MensajeBodyBroadcaster> {
        return InitialApplication.BroadcastServiceGlobalTasks2.pushpost(post)
    }
    suspend fun getcustompush(post: MsgBodyUser):Response<MsgBodyUser> {
        return InitialApplication.BroadcastServiceGlobalTasks2.pushrequest(post)
    }



}
