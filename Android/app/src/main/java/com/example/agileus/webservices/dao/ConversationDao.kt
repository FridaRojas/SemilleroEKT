package com.example.agileus.webservices.dao


import com.example.agileus.config.InitialApplication
import com.example.agileus.models.Conversation
import retrofit2.Response


class ConversationDao() {

        fun  recuperarPublicaciones(): ArrayList<Conversation> {

        val callRespuesta = InitialApplication.webServiceGlobal.getConversationOnetoOne()
        var ResponseDos:Response<ArrayList<Conversation>> = callRespuesta.execute()

        var lista = ArrayList<Conversation>()
       if (ResponseDos.isSuccessful){
               lista = ResponseDos.body()!!
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
