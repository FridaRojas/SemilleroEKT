package com.example.agileus.webservices.dao


import android.util.Log
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.Contacts
import com.example.agileus.models.Conversation
import com.example.agileus.models.Groups
import com.example.agileus.providers.ConversationProviderListener
import retrofit2.Response


class ConversationDao() {


  /* suspend fun  recuperarListadeContactos(): ArrayList<Contacts> {
       val callRespuesta = InitialApplication.webServiceConversation.getListContacts()
       var ResponseDos:Response<ArrayList<Contacts>> = callRespuesta.execute()

       var lista = ArrayList<Contacts>()
       if (ResponseDos.isSuccessful){
           lista = ResponseDos.body()!!
       }else{
          var prueba = Contacts("1","luz@gmail.com","efwefwe","rgg","regrege","ffefw","fwefewf","rrwd","wegrgrg", "gwrgregrg", "fwefef")
           lista.add(prueba)
           Log.e("ERROR", ResponseDos.code().toString())
       }
       return lista

   }

   */
  suspend fun  recuperarListadeContactos(idUser:String): ArrayList<Contacts> {
      val callRespuesta = InitialApplication.webServiceConversation.getListContacts(idUser)
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
        val callRespuesta = InitialApplication.webServiceConversation.getListGroups()
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

