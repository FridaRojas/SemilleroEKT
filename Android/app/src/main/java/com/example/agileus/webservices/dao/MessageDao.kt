package com.example.agileus.webservices.dao


import android.util.Log
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.*
import retrofit2.Response

class MessageDao {
    lateinit var respuesta: MessageResponse
    var listaMensajes = ArrayList<Conversation>()
    var lista = ArrayList<Contacts>()
    var listaGrupos = ArrayList<Groups>()

    suspend fun recuperarMensajes(idChat: String): ArrayList<Conversation> {
        try{
            val callRespuesta =
                InitialApplication.webServiceMessage.getConversationOnetoOne(idChat)
            var ResponseDos: Response<ArrayList<Conversation>> = callRespuesta.execute()


            if (ResponseDos.isSuccessful) {
                listaMensajes = ResponseDos.body()!!
            }

        }catch (ex:Exception){

        }

        return listaMensajes
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

        }
        return respuesta
    }


    suspend fun  recuperarListadeContactos(idUser:String): ArrayList<Contacts> {
        try{
            val callRespuesta = InitialApplication.webServiceMessage.getListContacts(idUser)
            var ResponseDos:Response<ArrayList<Contacts>> = callRespuesta.execute()


            if (ResponseDos.isSuccessful){
                lista = ResponseDos.body()!!
            }else{
                Log.e("ERROR", ResponseDos.code().toString())
            }

        }catch (ex:Exception){

        }
        return lista
    }

    suspend fun  recuperarListadeGrupos(): ArrayList<Groups> {
        try{
            val callRespuesta = InitialApplication.webServiceMessage.getListGroups()
            var ResponseDos:Response<ArrayList<Groups>> = callRespuesta.execute()


            if (ResponseDos.isSuccessful){
                listaGrupos = ResponseDos.body()!!
            }else{
                Log.e("ERROR", ResponseDos.code().toString())
            }

        }catch (ex:Exception){

        }

        return listaGrupos
    }
}