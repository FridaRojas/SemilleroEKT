package com.example.agileus.webservices.apis

import com.example.agileus.Models.Buzon
import com.example.agileus.models.Conversation
//import com.example.agileus.models.Conversation
import retrofit2.Call
import retrofit2.http.GET

interface ConversationApi {

    @GET("conversacion/getOne2/id2_id40")
 //   @GET("servicio.json?alt=media&token=e3076cf4-2c04-4609-ab51-8d3cbffdc6d8")
    fun getConversationOnetoOne(): Call<ArrayList<Conversation>>

}