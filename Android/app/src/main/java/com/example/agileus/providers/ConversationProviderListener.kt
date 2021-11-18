package com.example.agileus.providers

import com.example.agileus.models.Conversation

interface ConversationProviderListener {

    fun consumoSucessfull(lista:ArrayList<Conversation>)

    fun consumoFail(mensaje:String)
}