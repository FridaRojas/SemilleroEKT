package com.example.agileus.webservices.apis

import com.example.agileus.Models.Buzon
import com.example.agileus.models.Conversation
//import com.example.agileus.models.Conversation
import retrofit2.Call
import retrofit2.http.GET

interface ConversationApi {

    fun getmensajesbuzon(): Call<ArrayList<Buzon>>
}