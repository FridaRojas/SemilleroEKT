package com.example.agileus.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

object Constantes {
    val URL_ENVIAR_MENSAJE = "http://10.97.1.178:3040/api/"
    val URL_BUZON_1=""
    val URL_BUZON_2=""
    val URL_Tareas_Enviadas=""
    val URL_Tareas_Recibidas=""
    val id = "618e8743c613329636a769aa"
    val idChat = "618e8743c613329636a769aa_618b05c12d3d1d235de0ade0"
    val referenciaMensajeria = "Mensajeria"
    var URL_BASE1 = "https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/"
    //var URL_REPORTES_MENSAJES="https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/"
    //var URL_REPORTES_TAREAS="https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/"

    var URL_REPORTES_MENSAJES="https://firebasestorage.googleapis.com/v0/b/uberclone-fc437.appspot.com/o/"
    var URL_REPORTES_TAREAS="https://firebasestorage.googleapis.com/v0/b/uberclone-fc437.appspot.com/o/"

    var URL_BASE2 = "https://firebasestorage.googleapis.com/v0/b/minichat-8a171.appspot.com/o/"

    val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault())
    @RequiresApi(Build.VERSION_CODES.O)
    val date: LocalDateTime = LocalDateTime.now()
    val currentLocalTime = calendar.time
    val formatt: DateFormat = SimpleDateFormat("ZZZZZ", Locale.getDefault())
    val localTime: String = formatt.format(currentLocalTime)
    val finalDate = "$date$localTime"

}