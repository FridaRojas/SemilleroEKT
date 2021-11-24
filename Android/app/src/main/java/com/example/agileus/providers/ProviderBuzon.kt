package com.example.agileus.webservices.dao
import com.example.agileus.Models.Buzon
import com.example.agileus.config.InitialApplication
import com.example.agileus.providers.BuzonProviderListener
import retrofit2.Response

 class ProviderBuzon : BuzonProviderListener {
     suspend fun recuperarbuzon(lista:ArrayList<Buzon>):ArrayList<Buzon> {
       var extra= DaoBuzon(this)
        val callRespuesta = InitialApplication.BroadcastServiceGlobalTasks.getmensajesbuzon()
         var lista2=extra.obtener(callRespuesta,lista)
         return lista2
    }

 override suspend fun recibebuzon(ResponseDos:Response<ArrayList<Buzon>>,lista:ArrayList<Buzon>): ArrayList<Buzon> {
    var listaconsumida=lista
        if (ResponseDos.isSuccessful){
            listaconsumida = ResponseDos.body()!!
        }
        return listaconsumida
    }
}

