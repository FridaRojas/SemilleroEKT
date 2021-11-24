package com.example.agileus.webservices.dao

import android.util.Log
import android.widget.Toast
import com.example.agileus.config.InitialApplication
import com.example.agileus.config.InitialApplication.Companion.webServiceMessage
import com.example.agileus.models.Conversation
import com.example.agileus.models.Message
import com.example.agileus.ui.HomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageDao {


    suspend fun  recuperarMensajes(): ArrayList<Conversation> {
        val callRespuesta = InitialApplication.webServiceConversation.getConversationOnetoOne()
        var ResponseDos:Response<ArrayList<Conversation>> = callRespuesta.execute()

        var lista = ArrayList<Conversation>()
        if (ResponseDos.isSuccessful){
            lista = ResponseDos.body()!!
        }
        return lista

    }


    suspend fun insertarMensajes(mensaje: Message){

        var callRespuesta = InitialApplication.webServiceMessage.mandarMensaje(mensaje)

        callRespuesta.enqueue(object: Callback<Message>{
            override fun onResponse(call: Call<Message>, response: Response<Message>) {
                if(response.isSuccessful){
                    if (response.body() != null){
                         var nueva:Message= response.body()!!
                         var mensaje = "idEmisor: ${nueva.idEmisor}"
                         mensaje += "\n idReceptor: ${nueva.idReceptor}"
                         mensaje += "\n mensaje: ${nueva.texto}"
                         mensaje += "\n fecha: ${nueva.fechaCreacion}"

                        Log.e("mensaje", "$mensaje")


                    }
                    else{
                        Log.e("Error", "No se inserto ${response.code()}")
                       // Toast.makeText(activity as HomeActivity, "No se inserto ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }else{
                    Log.e("Error", "No se inserto ${response.code()}")
                   // Toast.makeText(applicationContext, "No se inserto ${response.code()}", Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<Message>, t: Throwable) {
            Log.e("Servidor", "El servidor ha fallado")
                //Toast.makeText(applicationContext, "El servidor ha fallado", Toast.LENGTH_LONG).show()
            }

        })

    }
}