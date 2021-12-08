package com.example.agileus.webservices.dao
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.agileus.R
import com.example.agileus.config.InitialApplication
import com.example.agileus.config.MySharedPreferences.reportesGlobales.fechaFinEstadisticas
import com.example.agileus.config.MySharedPreferences.reportesGlobales.fechaIniEstadisticas
import com.example.agileus.config.MySharedPreferences.reportesGlobales.idUsuarioEstadisticas
import com.example.agileus.config.MySharedPreferences.reportesGlobales.id_broadcast
import com.example.agileus.models.*
import retrofit2.Response
import java.time.ZonedDateTime

class ReporteMensajesDao {

    private var contador_mensajes_enviados:Int = 0
    private var contador_mensajes_recibidos:Int = 0
    private var recibidos_broadcast:Int=0
    private var contador_mensajes_leidos:Int=0
    private var contador_mensajes_totales:Int=0
    private var enviados_al_B:Int=0
    private lateinit var fecha_inicio: ZonedDateTime
    private lateinit var fecha_fin: ZonedDateTime
    lateinit var fecha_actual: ZonedDateTime

    var employeeList = ArrayList<Contacts>()
    var stadisticEmployeesList = ArrayList<UserMessageDetailReports>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun recuperardatosMensajes(idBusqueda: String): ArrayList<Estadisticas> {

        val listaRecycler= ArrayList<Estadisticas>()
        val messageSelectedStadistic = recoverUserMessageStadistic(idBusqueda, MySharedPreferences.idUsuarioEstadisticas)

        contador_mensajes_enviados= messageSelectedStadistic.send
        contador_mensajes_recibidos=messageSelectedStadistic.received
        contador_mensajes_totales=messageSelectedStadistic.total
        contador_mensajes_leidos=messageSelectedStadistic.read
        recibidos_broadcast= messageSelectedStadistic.receivedBroadcast
        enviados_al_B = messageSelectedStadistic.sendBroadcast

        listaRecycler.add(Estadisticas("Enviados:",contador_mensajes_enviados.toString(),"Recibidos:",contador_mensajes_recibidos.toString(), R.drawable.ic_pie_chart))
        listaRecycler.add(Estadisticas("Enviados al Broadcast:",enviados_al_B.toString(),"Recibidos del Broadcast:",recibidos_broadcast.toString(), R.drawable.ic_bar_chart))

        return listaRecycler

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun recoverUserMessageStadistic(idBusqueda: String, searchName: String): UserMessageDetailReports {

        val callRespuesta = InitialApplication.webServiceGlobalReportes.getDatosReporteMensajes(idBusqueda)
        val ResponseMensajes: Response<conversartionListByID> = callRespuesta.execute()

        val callRespuestaBroadCast = InitialApplication.webServiceGlobalReportesBroadCast.getDatosRespuestasBroadcast(idBusqueda)
        val ResponseMensajesBroadCast: Response<ArrayList<DatosBroadCast>> = callRespuestaBroadCast.execute()

        var lista: ArrayList<Conversation>
        var lista_B: ArrayList<DatosBroadCast>
        var messageDetail = UserMessageDetailReports()

        val id_usuario_actual = idBusqueda //Aquí se coloca el id del emisor deseado
        var contador_m_enviados= 0
        var contador_m_recibidos = 0
        var contador_m_leidos=0
        var contador_m_totales=0
        //val id_BroadCast = id_broadcast// id del Broadcast
        var contador_recibidos_B = 0
        var broadcastSize = 0

        //Fechas por día, mes, año, custom y default
        fecha_inicio = ZonedDateTime.parse(fechaIniEstadisticas)
        fecha_fin = ZonedDateTime.parse(fechaFinEstadisticas)
        if (idBusqueda == "TEAM_ID_CREATED_BY_MOD_REPORT"){
            messageDetail = MySharedPreferences.empleadoUsuario[MySharedPreferences.empleadoUsuario.size-1]
        }else if (ResponseMensajes.isSuccessful || ResponseMensajesBroadCast.isSuccessful) {
            Log.d("messageORbroadcast", "Success")
            try {
                val listaDS = ResponseMensajes.body()!!
                lista = listaDS.data
                lista.forEach {

                    fecha_actual = ZonedDateTime.parse(it.fechaEnviado)
                    if((fecha_actual.isEqual(fecha_inicio) || fecha_actual.isAfter(fecha_inicio)) &&
                        (fecha_actual.isBefore(fecha_fin))) {
                        contador_m_totales = contador_m_totales + 1

                        if (id_usuario_actual == it.idemisor) {
                            contador_m_enviados = contador_m_enviados + 1
                        }
                        if (id_usuario_actual == it.idreceptor){
                            contador_m_recibidos = contador_m_recibidos + 1
                        }

                        if (it.statusLeido == "true") {
                            contador_m_leidos = contador_m_leidos + 1
                        }

                        if (it.idemisor == id_broadcast) {
                            contador_recibidos_B += 1
                        }
                    }
                }
            }catch (ex: java.lang.Exception){
                Log.d("RMDao NoMessageConexion", ex.toString())
            }

            //Mensajes enviados al Broadcast por Usuario
            try {
                lista_B = ResponseMensajesBroadCast.body()!!
                broadcastSize = lista_B.size
            }catch (ex: java.lang.Exception){
                Log.d("RMDao NoBroadcastConexion", ex.toString())
            }

            messageDetail = UserMessageDetailReports(
                idBusqueda,
                searchName,
                contador_m_enviados,
                contador_m_recibidos,
                contador_m_leidos,
                contador_m_totales,
                broadcastSize,
                contador_recibidos_B
            )
        }
        /*

        else if (ResponseMensajes.isSuccessful) {
            val listaDS = ResponseMensajes.body()!!
            lista = listaDS.data

            lista.forEach {

                fecha_actual = ZonedDateTime.parse(it.fechaEnviado)

                if((fecha_actual.isEqual(fecha_inicio) || fecha_actual.isAfter(fecha_inicio)) &&
                    (fecha_actual.isBefore(fecha_fin))) {
                    contador_m_totales = contador_m_totales + 1

                    if (id_usuario_actual == it.idemisor) {
                        contador_m_enviados = contador_m_enviados + 1
                    }
                    if (id_usuario_actual == it.idreceptor){
                        contador_m_recibidos = contador_m_recibidos + 1
                    }

                    if (it.statusLeido) {
                        contador_m_leidos = contador_m_leidos + 1
                    }

                    if (it.idemisor == id_broadcast) {
                        contador_recibidos_B += 1
                    }
                }
            }
            //Mensajes enviados al Broadcast por Usuario
            messageDetail = UserMessageDetailReports(
                idBusqueda,
                searchName,
                contador_m_enviados,
                contador_m_recibidos,
                contador_m_leidos,
                contador_m_totales,
                0,
                contador_recibidos_B
            )
        }
         */
        return messageDetail
    }


    fun obtenerMensajesEnviados():String{

        return contador_mensajes_enviados.toString()
    }

    fun obtenerMensajesRecibidos():String{

        return contador_mensajes_recibidos.toString()
    }

    fun obtenerMensajesTotales():String{

        return contador_mensajes_totales.toString()
    }

    fun obtenerMensajesLeidos():String{

        return contador_mensajes_leidos.toString()
    }
    
@RequiresApi(Build.VERSION_CODES.O)
fun obtenerListaSubContactos(idUser:String): ArrayList<UserMessageDetailReports> {
        try{
            val callRespuesta = InitialApplication.webServiceGlobalReportes.getListSubContacts( idUser)
            var ResponseDos:Response<EmployeeListByBossID> = callRespuesta.execute()

            if (ResponseDos.isSuccessful){
                val listaConsumida = ResponseDos.body()!!
                employeeList = listaConsumida.dataEmployees

                stadisticEmployeesList.add(recoverUserMessageStadistic(idUser, "Mi información"))

                //Obtencion de estadisticas de los empleados
                if(employeeList.isNotEmpty()){
                    employeeList.forEach {
                        stadisticEmployeesList.add(recoverUserMessageStadistic(it.id, it.nombre))
                        Log.d("ListaSubConactsm", "id: ${it.id}")
                        Log.d("ListaSubConactsm", "Nombre: ${it.nombre}")
                    }
                    stadisticEmployeesList.add(totalGroupEstadisticsBYBoss(stadisticEmployeesList))
                    stadisticEmployeesList.forEach {
                        Log.d("ListaSubConactsDetailm", "id: ${it.id}")
                        Log.d("ListaSubConactsDetailm", "Nombre: ${it.name}")
                        Log.d("ListaSubConactsDetailm", "send: ${it.send}")
                        Log.d("ListaSubConactsDetailm", "received: ${it.received}")
                        Log.d("ListaSubConactsDetailm", "total: ${it.total}")
                    }
                }

                Log.d("ListaSubConactsSIZE", "SIZE: ${stadisticEmployeesList.size}")
            }else{
                Log.e("ERROR SubContactos", "Respuesta fallida:" + ResponseDos.code().toString())
            }

        }catch (ex:Exception){
            Log.e("ERROR SubContactos", "Error al iniciar servicio")
        }
        return stadisticEmployeesList
    }


    fun totalGroupEstadisticsBYBoss(dataValues: ArrayList<UserMessageDetailReports>): UserMessageDetailReports{
        var send: Int = 0
        var received: Int = 0
        var read: Int = 0
        var total: Int = 0
        var sendBroadcast: Int = 0
        var receivedBroadcast: Int = 0

        for (data in dataValues.slice(1..(dataValues.size - 1))){
            Log.d("GROP", data.name)
            send = send     + data.send
            received    = received  + data.received
            read        = read      + data.read
            total       = total     + data.total
            sendBroadcast   = sendBroadcast + data.sendBroadcast
        }
        var groupStadistic = UserMessageDetailReports(
            "TEAM_ID_CREATED_BY_MOD_REPORT",
            "Mi equipo",
            send,
            received,
            read,
            total,
            sendBroadcast,
            receivedBroadcast
        )
        return groupStadistic

    }



}