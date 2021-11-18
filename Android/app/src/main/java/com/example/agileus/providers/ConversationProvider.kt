package com.example.agileus.providers

import com.example.agileus.models.Conversation
import com.example.agileus.webservices.dao.ConversationDao

class ConversationProvider : ConversationProviderListener{

    var objeto = ConversationDao(this)

    fun obtenerlistaConversaciones(){

        objeto.recuperarPublicaciones()

    }

    override fun consumoSucessfull(lista: ArrayList<Conversation>) {

    }

    override fun consumoFail(mensaje: String) {

    }


}