package com.example.agileus.utils


import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.agileus.models.UserMessageDetailReport
import com.example.agileus.models.UserTaskDetailReport
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*


object Constantes {
    val URL_ENVIAR_MENSAJE = "http://3.144.86.49:8080/Servicios-0.0.1-SNAPSHOT/api/"
    val URL_BUZON_1=""
    val URL_BUZON_2=""
    val URL_Tareas_Enviadas=""
    val URL_Tareas_Recibidas=""

    val idUser = "618b05c12d3d1d235de0ade0"
    //var id = "61a83b59d036090b8e8db3c1"
    //var id = "618d9c26beec342d91d747d6"
    val idChat = "618d9c26beec342d91d747d6_618b05c12d3d1d235de0ade0"
    val referenciaMensajeria = "Mensajeria"
    val referenciaTareas= "Tareas"
    val URL_BASE_TAREAS = "http://3.144.86.49:8080/Servicios-0.0.1-SNAPSHOT/api/"
    //val URL_BASE_TAREAS = "http://10.97.6.83:3040/api/"
    var URL_BASE1 = "https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/"

    //var URL_REPORTES="https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/"
    var URL_REPORTES="http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/api/"

    //var URL_BASE2 = "https://firebasestorage.googleapis.com/v0/b/minichat-8a171.appspot.com/o/"
//    var URL_BASE2 = "https://us-central1-demoapirestbroadcast.cloudfunctions.net/app/"
    val URL_BASE2 = "https://us-central1-demoapirestbroadcast.cloudfunctions.net/app/"
  //  val URL_BASE3 = "http://3.144.86.49:8080/Servicios-0.0.1-SNAPSHOT/api/"
    var broadlist = ""
    val URL_BASE3 = "http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/api/"
   // val URL_BASE3 = "http://3.144.86.49:8080/Servicios-0.0.1-SNAPSHOT/api/"
    val URL_BASE4 = "http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/api/"
    //val broadlist = "61a101db174bcf469164d2fd"
    val userlistbuzon="618e8821c613329636a769ac"
    var CURRENT_USER ="618e8743c613329636a769aa"
    //var URL_BASE2 = "https://firebasestorage.googleapis.com/v0/b/minichat-8a171.appspot.com/o/"
    val ID_RECEPTOR = "ID_RECEPTOR"
    val CHAT_GROUP = "CHAT_GROUP"
    val ID_CHAT = "ID_CHAT"
    val ROL_USER = "ROL_USER"
   // val NAME_RECEPTOR = "NAME_RECEPTOR"
    //val idChat = "618e878ec613329636a769ab_618e8821c613329636a769a1"
   // val URL_BASE_TAREAS = "http://10.97.3.134:2021/api/"
   // var URL_BASE1 = "https://firebasestorage.googleapis.com/v0/b/uber-test-c9f54.appspot.com/o/"
 //   var URL_BASE2 = "https://us-central1-demoapirestbroadcast.cloudfunctions.net/app/"
    val NAME_RECEPTOR = "NAME_RECEPTOR"

    val URL_Tasks_Personas="http://3.144.86.49:8080/Servicios-0.0.1-SNAPSHOT/api/user/findByBossId/"

    val URL_Tasks_Personas2="http://3.144.86.49:8080/Servicios-0.0.1-SNAPSHOT/api/user/"

    val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault())
    @RequiresApi(Build.VERSION_CODES.O)
    val date: LocalDateTime = LocalDateTime.now()
    @RequiresApi(Build.VERSION_CODES.O)
    val zonedDate: ZonedDateTime = ZonedDateTime.now()
    val currentLocalTime = calendar.time
    val formatt: DateFormat = SimpleDateFormat("ZZZZZ", Locale.getDefault())
    val localTime: String = formatt.format(currentLocalTime)
    val finalDate = "$date$localTime"


    @RequiresApi(Build.VERSION_CODES.O)
    fun devuelveFechaHora(fecha:String):String{
        var minute = ""
        var time = ZonedDateTime.parse(fecha)

        var hour = time.hour + 6

        if(time.minute < 10){
            minute = "0"+time.minute.toString()
        }else{
            minute = time.minute.toString()
        }
        return time.dayOfMonth.toString()+"-"+time.monthValue +"-"+time.year+"  " +hour.toString()+":" + minute

    }
    //Login
  //  val URL_LOGIN = "https://firebasestorage.googleapis.com/v0/b/pruebas-eqipo-admin.appspot.com/o/"
      val URL_Login = "http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/"                // "http://3.144.86.49:8080/Servicios-0.0.1-SNAPSHOT/"            // "https://10.97.3.183:3040/"        // "http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/" //"http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/"//"https://firebasestorage.googleapis.com/v0/b/pruebas-eqipo-admin.appspot.com/o/"           // "http://10.97.4.193:3040/"                                    // "http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/"                    // "http://10.97.6.83:3040"                       // "http://18.218.7.148:3040"                                                                                 // "http://10.97.2.198:3040"                    //"https://10.97.6.83"       //"http://10.97.2.202:3040"
    //val URL_Login = "http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/"//"https://firebasestorage.googleapis.com/v0/b/pruebas-eqipo-admin.appspot.com/o/"           // "http://10.97.4.193:3040/"                                    // "http://ec2-3-144-86-49.us-east-2.compute.amazonaws.com:8080/Servicios-0.0.1-SNAPSHOT/"                    // "http://10.97.6.83:3040"                       // "http://18.218.7.148:3040"                                                                                 // "http://10.97.2.198:3040"                    //"https://10.97.6.83"       //"http://10.97.2.202:3040"
   // val URL_Login = "http://3.144.86.49:8080/Servicios-0.0.1-SNAPSHOT/api/"                                                                               // "http://10.97.2.198:3040"                    //"https://10.97.6.83"       //"http://10.97.2.202:3040"

    //var idUsuario = "618e8743c613329636a769aa"
    var idUsuario =idUser
    //var idUsuarioEstadisticas = idUsuario
    val GROUP_ID_REPORTES ="TEAM_ID_CREATED_BY_MOD_REPORT"
    //var idUsuario = id
    var idUsuarioEstadisticas = GROUP_ID_REPORTES
    var opcionFiltro = 0
    var fechaIniCustomEstadisticas = "1970-01-01T00:00:00.000+00:00"
    var fechaEstadisticas = Constantes.date.toString()
    var id_broadcast="618d9c26beec342d91d747d6"
    var fechaIniEstadisticas = "1900-01-01T00:00:00.000+00:00"
    var fechaFinEstadisticas = "2100-01-01T00:00:00.000+00:00"
    var empleadoUsuario = emptyList<UserMessageDetailReport>()
    var dataEmpleadoUsuario = emptyList<UserTaskDetailReport>()
    var tipo_grafica:Int=0
    var vista:Int=0

}

