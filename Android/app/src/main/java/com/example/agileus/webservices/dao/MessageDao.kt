package com.example.agileus.webservices.dao

import android.widget.Toast
import com.example.agileus.models.Conversation
import com.example.agileus.models.Message
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageDao {
/*
    fun insertarPublicacion(mensaje:String, fecha:String) {
        var mMensajePrueba = Message("11", "12", mensaje, fecha)
        webServiceMessage.mandarMensaje(mMensajePrueba).execute()
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