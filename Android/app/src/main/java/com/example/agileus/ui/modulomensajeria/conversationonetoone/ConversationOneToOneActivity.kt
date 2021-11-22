package com.example.agileus.ui.modulomensajeria.conversationonetoone

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.databinding.ActivityConversationOneToOneBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

class ConversationOneToOneActivity : AppCompatActivity() {

    lateinit var binding:ActivityConversationOneToOneBinding
    lateinit var conversationviewModel:ConversationViewModel

    @RequiresApi(Build.VERSION_CODES.O)
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

        binding.btnEnviarMensaje.setOnClickListener {
            val calendar = Calendar.getInstance(
                TimeZone.getTimeZone("GMT"),
                Locale.getDefault()
            )

            val date: LocalDateTime = LocalDateTime.now()
            val currentLocalTime = calendar.time
            val formatt: DateFormat = SimpleDateFormat("ZZZZZ", Locale.getDefault())
            val localTime: String = formatt.format(currentLocalTime)
           // conversationviewModel.mandarMensaje(binding.etMensaje.text.toString(),"${fech}" )

            Toast.makeText(applicationContext, "$date$localTime", Toast.LENGTH_LONG).show()
        }

    }
}