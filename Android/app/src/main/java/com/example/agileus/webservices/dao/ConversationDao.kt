package com.example.agileus.webservices.dao

import android.util.Log
import android.widget.Toast
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.Conversation
import com.example.agileus.providers.ConversationProviderListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConversationDao(val listener:ConversationProviderListener) {

    fun recuperarPublicaciones(){
        var callRespuesta= InitialApplication.webServiceGlobal.getConversationOnetoOne()
        callRespuesta.enqueue(object: Callback<ArrayList<Conversation>> {
            override fun onResponse(
                call: Call<ArrayList<Conversation>>,
                response: Response<ArrayList<Conversation>>
            ) {
                if(response.isSuccessful){
                    val listaConsumida = response.body()
                    if(listaConsumida !=null && listaConsumida.size>0){
                        listener.consumoSucessfull(listaConsumida)

                        //listaConsumida.forEach {
                          //  var id = it.id
                          //  var fecha = it.fecha
                          //  Log.i("Mensaje","El id es: $id. la Fecha es: $fecha")

                            //Toast.makeText(applicationContext,"El id es: $id. la Fecha es: $fecha", Toast.LENGTH_SHORT).show()
                       // }

                    }
                    else {

                        listener.consumoFail(response.code().toString())

                    }

                }
            }

            override fun onFailure(call: Call<ArrayList<Conversation>>, t: Throwable) {
                //Toast.makeText(applicationContext,"Falló petición del servidor", Toast.LENGTH_SHORT).show()
                listener.consumoFail(t.message.toString())
                Log.d("Error",t.message.toString())

            }


        })
    }

}
