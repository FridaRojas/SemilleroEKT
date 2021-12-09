package com.example.agileus.webservices.dao

import android.util.Log
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.*
import com.example.agileus.models.response.ResponseChats
import com.example.agileus.models.response.ResponseContacts
import com.example.agileus.models.response.ResponseConversation
import com.example.agileus.models.response.ResponseGroups
import retrofit2.Response

class MessageDao {
    lateinit var respuesta: MessageResponse


    suspend fun recuperarMensajes(idUser:String, idChat: String): ArrayList<Conversation> {
        lateinit var responseConversation: ResponseConversation
        var listMensajes = ArrayList<Conversation>()
        try{
            val callRespuesta =
                InitialApplication.webServiceMessage.getConversationOnetoOne(idUser,idChat)
            val responseCallConversation = callRespuesta.execute()

    if(responseCallConversation != null){
        if(responseCallConversation.isSuccessful){
            responseConversation = responseCallConversation.body()!!
            if(responseConversation.data != null){
                listMensajes = responseConversation.data
            }else{
                listMensajes = emptyList<Conversation>() as ArrayList<Conversation>
            }
        }else{

        }
    }else{

    }
        }catch (ex:Exception){
            Log.e("ErrorRecuperarMensajes", ex.message.toString())
        }

        return listMensajes
    }

    suspend fun insertarMensajes(mensaje: Message): MessageResponse {
        try{
            var callRespuesta = InitialApplication.webServiceMessage.mandarMensaje(mensaje)
            var ResponseDos: Response<MessageResponse> = callRespuesta.execute()

            if(ResponseDos.isSuccessful){
                respuesta = ResponseDos.body()!!
            }else{

            }

        }catch (ex:Exception){
                Log.e("ErrorInsertarMensaje", ex.message.toString())
        }
        return respuesta
    }


    suspend fun  recuperarListadeContactos(idUser:String): ArrayList<Contacts> {
        var listContacts = ArrayList<Contacts>()
        lateinit var responseContacts: ResponseContacts
        try{
            val callRespuesta = InitialApplication.webServiceMessage.getListContacts(idUser)
            var responseCallContacts = callRespuesta.execute()

        if(responseCallContacts != null){
          if(responseCallContacts.isSuccessful){
             responseContacts = responseCallContacts.body()!!
            if(responseContacts.data != null){
            listContacts = responseContacts.data
                  }else{

                  }
          }else{

      }
        }else{

        }


        }catch (ex:Exception){
            Log.e("ErrorRecuperarListadeContactos", ex.message.toString())
        }
        return listContacts
    }

    suspend fun  recuperarListadeGrupos(idUser: String): ArrayList<Groups> {
        var listGroups = ArrayList<Groups>()
        lateinit var responseGroups: ResponseGroups
        try{
            val callRespuesta = InitialApplication.webServiceMessage.getListGroups(idUser)
            var responseCallGroups = callRespuesta.execute()


            if(responseCallGroups != null){
                if(responseCallGroups.isSuccessful){
                    responseGroups = responseCallGroups.body()!!
                    if(responseGroups.data != null){
                        listGroups = responseGroups.data
                    }else{

                    }
                }else{

                }

            }else{

            }

        }catch (ex:Exception){
            Log.e("ErrorRecuperarListadeGrupos", ex.message.toString())
        }

        return listGroups
    }

    suspend fun  recuperarListadeChats(idUser: String): ArrayList<Chats> {
        var listaChats = ArrayList<Chats>()
        lateinit var responseChats: ResponseChats

        try{
            val callRespuesta = InitialApplication.webServiceMessage.getListChats(idUser)
            var responseCallChats = callRespuesta.execute()

            if(responseCallChats != null){
                if(responseCallChats.isSuccessful){
                    responseChats = responseCallChats.body()!!
                    if(responseChats.data != null){
                        listaChats = responseChats.data
                    }else{

                    }

                }else{

                }
            }else{

            }

        }catch (ex:Exception){
            Log.e("ErrorRecuperarListadeChats", ex.message.toString())
        }

        return listaChats
    }

    suspend fun actualizarStatus(idUser:String,statusRead: StatusRead): MessageResponse {
        try{
            var callRespuesta = InitialApplication.webServiceMessage.statusUpdate(statusRead,idUser)
            var ResponseDos: Response<MessageResponse> = callRespuesta.execute()

            if(ResponseDos.isSuccessful){
                respuesta = ResponseDos.body()!!
            }else{

            }

        }catch (ex:Exception){
            Log.e("ErrorAlActualizarLeido ", ex.message.toString())
        }
        return respuesta
    }
}