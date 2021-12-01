package com.example.agileus.webservices.dao
import com.example.agileus.models.Buzon
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.ListaUsers
import com.example.agileus.providers.BuzonProviderListener
import retrofit2.Response


 class ProviderBuzon : BuzonProviderListener {

     suspend fun recuperarbuzon(lista:ArrayList<Buzon>):ArrayList<Buzon> {
       var extra= DaoBuzon(this)
        val callRespuesta = InitialApplication.BroadcastServiceGlobalTasks.getmensajesbuzon()
         var lista2=extra.obtener(callRespuesta,lista)
         return lista2
    }

     override suspend fun recibebuzon(ResponseDos:Response<ArrayList<Buzon>>, lista:ArrayList<Buzon>): ArrayList<Buzon> {

         var listaconsumida=lista
         if (ResponseDos.isSuccessful){
             listaconsumida = ResponseDos.body()!!
         }
         return listaconsumida
     }



     suspend fun listausuarios(lista:ArrayList<ListaUsers>):ArrayList<ListaUsers>
     {
         var extra= DaoBuzon(this)
         val callRespuesta = InitialApplication.BroadcastServiceGlobalTasks2.getList()
         var lista2=extra.getlist(callRespuesta,lista)
         return lista2
     }



     suspend fun pushPost(buzon:Buzon):Response<Buzon>
  {
      return InitialApplication.BroadcastServiceGlobalTasks.pushpost(buzon)
  }



 override suspend fun getLista(ResponseDos: Response<ArrayList<ListaUsers>>,lista: ArrayList<ListaUsers>): ArrayList<ListaUsers> {
         var listaconsumida=lista
         if (ResponseDos.isSuccessful){
             listaconsumida = ResponseDos.body()!!
         }
         return listaconsumida
     }





}

