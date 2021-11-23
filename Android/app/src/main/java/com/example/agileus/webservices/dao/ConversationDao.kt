package com.example.agileus.webservices.dao


import android.util.Log
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.Contacts
import com.example.agileus.models.Conversation
import com.example.agileus.providers.ConversationProviderListener
import retrofit2.Response


class ConversationDao() {

   suspend fun  recuperarPublicaciones(): ArrayList<Conversation> {
        val callRespuesta = InitialApplication.webServiceConversation.getConversationOnetoOne()
        var ResponseDos:Response<ArrayList<Conversation>> = callRespuesta.execute()

        var lista = ArrayList<Conversation>()
       if (ResponseDos.isSuccessful){
               lista = ResponseDos.body()!!
       }
        return lista

    }


   suspend fun  recuperarListadeContactos(): ArrayList<Contacts> {
       val callRespuesta = InitialApplication.webServiceListContacts.getListContacts()
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

}

     /*
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

}*/
