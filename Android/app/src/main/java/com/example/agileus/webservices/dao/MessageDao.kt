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

   /* suspend fun  insertarPublicacion(mensaje: Message){
        val callRespuesta = InitialApplication.webServiceMessage.mandarMensaje(mensaje)
        var ResponseDos = callRespuesta.execute()

       if(ResponseDos.isSuccessful){
         Log.i("EXITOSO", "REGISTRO EXITOSO")
       }else{
           Log.i("NOEXITOSO", "No fue exitoso")
       }
    }

    */


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

/*
    fun insertarPublicacion(mensaje:Message) {
       // var mMensajePrueba = Message("618d9c26beec342d91d747d6", "618d9c26beec342d91d747d6", mensaje, fecha)
        webServiceMessage.mandarMensaje(mensaje).execute()
        //var ResponseDos: Response<Message> = callRespuesta.execute()

    }



 */

/*
        callRespuesta.enqueue(object: Callback<Publicacion> {
            override fun onResponse(call: Call<Publicacion>, response: Response<Publicacion>) {
                if(response.isSuccessful){
                    if (response.body() != null){
                        /* var nueva:Publicacion = response.body()!!
                         var mensaje = "Publicacion creada con el id: ${nueva.id}"
                         mensaje += "\n titulo: ${nueva.title}"
                         mensaje += "\n userId: ${nueva.userId}"
                         mensaje += "\n body: ${nueva.body}"
                         Toast.makeText(applicationContext, "$mensaje", Toast.LENGTH_LONG).show()*/


                    }
                    else{
                        Toast.makeText(applicationContext, "No se inserto ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }else{
                    Toast.makeText(applicationContext, "No se inserto ${response.code()}", Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<Publicacion>, t: Throwable) {
                Toast.makeText(applicationContext, "El servidor ha fallado", Toast.LENGTH_LONG).show()
            }

        })

    }
    */

}