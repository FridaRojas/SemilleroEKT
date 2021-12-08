package com.example.agileus.utils


import android.os.Build
import androidx.annotation.RequiresApi
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


object Constantes {
    val URL_BASE_TAREAS = "http://10.97.5.172:2021/api/"
    val URL_ENVIAR_MENSAJE = "http://10.97.1.178:3040/api/"
    val URL_BUZON_1=""
    val URL_BUZON_2=""
    val URL_Tareas_Enviadas=""
    val URL_Tareas_Recibidas=""
    var id = "618e8743c613329636a769aa"
    val idChat = "618e8743c613329636a769aa_618b05c12d3d1d235de0ade0"
    val referenciaMensajeria = "Mensajeria"
    val referenciaTareas= "Tareas"
    var URL_BASE1 = "https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/"
//    var URL_BASE2 = "https://us-central1-demoapirestbroadcast.cloudfunctions.net/app/"
    val URL_BASE2 = "https://us-central1-demoapirestbroadcast.cloudfunctions.net/app/"
    val URL_BASE3 = "http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/api/"
    val broadlist = "61a101db174bcf469164d2fd"
    val userlistbuzon="618e8821c613329636a769ac"
    val CURRENT_USER ="618e8743c613329636a769aa"
    //var URL_BASE2 = "https://firebasestorage.googleapis.com/v0/b/minichat-8a171.appspot.com/o/"
    val ID_RECEPTOR = "ID_RECEPTOR"
    val CHAT_GROUP = "CHAT_GROUP"
    val ID_CHAT = "ID_CHAT"

    val URL_Tasks_Personas="http://10.97.3.134:3040/api/user/findByBossId/"

    val URL_Tasks_Personas2="http://10.97.3.134:3040/api/user/"

    val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault())
    @RequiresApi(Build.VERSION_CODES.O)
    val date: LocalDateTime = LocalDateTime.now()
    val currentLocalTime = calendar.time
    val formatt: DateFormat = SimpleDateFormat("ZZZZZ", Locale.getDefault())
    val localTime: String = formatt.format(currentLocalTime)
    val finalDate = "$date$localTime"

    //Login
    val URL_LOGIN = "https://firebasestorage.googleapis.com/v0/b/pruebas-eqipo-admin.appspot.com/o/"
    val URL_Login = "http://3.144.86.49:8080/Servicios-0.0.1-SNAPSHOT/"        // "http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/" //"http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/"//"https://firebasestorage.googleapis.com/v0/b/pruebas-eqipo-admin.appspot.com/o/"           // "http://10.97.4.193:3040/"                                    // "http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/"                    // "http://10.97.6.83:3040"                       // "http://18.218.7.148:3040"                                                                                 // "http://10.97.2.198:3040"                    //"https://10.97.6.83"       //"http://10.97.2.202:3040"



}

