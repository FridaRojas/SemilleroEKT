package com.example.agileus.webservices.dao


import android.util.Log
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.*
import retrofit2.Response

class MessageDao {
    lateinit var respuesta: MessageResponse

    suspend fun recuperarMensajes(idChat: String): ArrayList<Conversation> {
        val callRespuesta =
            InitialApplication.webServiceMessage.getConversationOnetoOne(idChat)
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


    suspend fun  recuperarListadeContactos(idUser:String): ArrayList<Contacts> {
        val callRespuesta = InitialApplication.webServiceMessage.getListContacts(idUser)
        var ResponseDos:Response<ArrayList<Contacts>> = callRespuesta.execute()

        var lista = ArrayList<Contacts>()
        if (ResponseDos.isSuccessful){
            lista = ResponseDos.body()!!
        }else{
            Log.e("ERROR", ResponseDos.code().toString())
        }
        return lista
    }

    suspend fun  recuperarListadeGrupos(): ArrayList<Groups> {
        val callRespuesta = InitialApplication.webServiceMessage.getListGroups()
        var ResponseDos:Response<ArrayList<Groups>> = callRespuesta.execute()

        var lista = ArrayList<Groups>()
        if (ResponseDos.isSuccessful){
            lista = ResponseDos.body()!!
        }else{
            Log.e("ERROR", ResponseDos.code().toString())
        }
        return lista

    }
}