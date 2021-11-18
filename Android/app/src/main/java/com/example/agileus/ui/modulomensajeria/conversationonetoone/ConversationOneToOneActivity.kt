package com.example.agileus.ui.modulomensajeria.conversationonetoone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.R
import com.example.agileus.databinding.ActivityConversationOneToOneBinding

class ConversationOneToOneActivity : AppCompatActivity() {

    lateinit var binding:ActivityConversationOneToOneBinding
    lateinit var conversationviewModel:ConversationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversationOneToOneBinding.inflate(layoutInflater)
        setContentView(binding.root)


        conversationviewModel = ViewModelProvider(this).get()
        conversationviewModel.devuelveLista()

        conversationviewModel.adaptador.observe(this,{
            binding.recyclerConversacion.adapter = it
            binding.recyclerConversacion.layoutManager = LinearLayoutManager(this)

        })
    }
}