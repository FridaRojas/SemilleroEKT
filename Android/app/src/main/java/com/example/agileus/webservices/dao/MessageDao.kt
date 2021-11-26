package com.example.agileus.webservices.dao


import com.example.agileus.config.InitialApplication
import com.example.agileus.models.Conversation
import com.example.agileus.models.Message
import com.example.agileus.models.MessageResponse
import retrofit2.Response

class MessageDao {
    lateinit var respuesta: MessageResponse

    suspend fun recuperarMensajes(idChat: String): ArrayList<Conversation> {
        val callRespuesta =
            InitialApplication.webServiceConversation.getConversationOnetoOne(idChat)
        var ResponseDos: Response<ArrayList<Conversation>> = callRespuesta.execute()

        var lista = ArrayList<Conversation>()
        if (ResponseDos.isSuccessful) {
            lista = ResponseDos.body()!!
        }
        return lista

    }

    suspend fun insertarMensajes(mensaje: Message): MessageResponse {
        var callRespuesta = InitialApplication.webServiceMessage.mandarMensaje(mensaje)
       var ResponseDos: Response<MessageResponse> = callRespuesta.execute()

        if(ResponseDos.isSuccessful){
        respuesta = ResponseDos.body()!!
        }else{

        }
            return respuesta
    }


    /*
    suspend fun insertarMensajes(mensaje: Message){

        var callRespuesta = InitialApplication.webServiceMessage.mandarMensaje(mensaje)

        callRespuesta.enqueue(object: Callback<MessageResponse>{
            override fun onResponse(call: Call<MessageResponse>, response: Response<MessageResponse>) {
                if(response.isSuccessful){
                    if (response.body() != null){
                         var nueva:MessageResponse= response.body()!!
                         var mensaje = "idEmisor: ${nueva.msj}"
                         mensaje += "\n idReceptor: ${nueva.status}"
                         mensaje += "\n mensaje: ${nueva.data}"


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

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
            Log.e("Servidor", "El servidor ha fallado")
                //Toast.makeText(applicationContext, "El servidor ha fallado", Toast.LENGTH_LONG).show()
            }

        })

    }

     */
}