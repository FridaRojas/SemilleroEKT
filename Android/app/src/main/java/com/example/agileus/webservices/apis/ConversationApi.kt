package com.example.agileus.webservices.apis

import com.example.agileus.models.Conversation
import retrofit2.Call
import retrofit2.http.GET

interface ConversationApi {

    @GET("conversacion/getOne2/id2_id40")
    fun getConversationOnetoOne(): Call<ArrayList<Conversation>>

}