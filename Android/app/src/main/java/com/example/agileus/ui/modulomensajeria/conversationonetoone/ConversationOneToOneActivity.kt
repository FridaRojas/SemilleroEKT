package com.example.agileus.ui.modulomensajeria.conversationonetoone

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.databinding.ActivityConversationOneToOneBinding
import com.example.agileus.models.Message
import com.example.agileus.ui.modulomensajeria.listacontactos.ConversationViewModel
import com.example.agileus.utils.Constantes
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
        conversationviewModel.devuelveLista(Constantes.idChat)

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

            var mensaje = Message(Constantes.id,"618b05c12d3d1d235de0ade0","","${binding.etMensaje.text.toString()}","$date$localTime")
            conversationviewModel.mandarMensaje(Constantes.idChat,mensaje)

}
        }
}