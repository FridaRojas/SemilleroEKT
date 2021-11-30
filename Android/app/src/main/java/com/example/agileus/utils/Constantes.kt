package com.example.agileus.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

object Constantes {
    val URL_ENVIAR_MENSAJE = "http://10.97.7.227:3040/api/"
    val URL_BUZON_1=""
    val URL_BUZON_2=""
    val URL_Tareas_Enviadas=""
    val URL_Tareas_Recibidas=""
    val id = "618e8743c613329636a769aa"
    val CHAT_NAME = "CHAT_NAME"
    val ID_RECEPTOR = "ID_RECEPTOR"
    val CHAT_GROUP = "CHAT_GROUP"
    val ID_CHAT = "ID_CHAT"
    val idChat = "618e878ec613329636a769ab_618e8821c613329636a769a1"
    val referenciaMensajeria = "Mensajeria"
    val URL_BASE_TAREAS = "http://10.97.3.134:2021/api/"
    var URL_BASE1 = "https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/"
    var URL_BASE2 = "https://us-central1-demoapirestbroadcast.cloudfunctions.net/app/"

    val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault())
    @RequiresApi(Build.VERSION_CODES.O)
    val date: LocalDateTime = LocalDateTime.now()
    val currentLocalTime = calendar.time
    val formatt: DateFormat = SimpleDateFormat("ZZZZZ", Locale.getDefault())
    val localTime: String = formatt.format(currentLocalTime)
    val finalDate = "$date$localTime"

}