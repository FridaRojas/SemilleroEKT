package com.example.agileus.webservices.dao
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.agileus.R
import com.example.agileus.config.InitialApplication
import com.example.agileus.config.InitialApplication.Companion.preferenciasGlobal
import com.example.agileus.models.*
import com.example.agileus.models.response.ResponseConversation
import com.example.agileus.utils.Constantes.TEAM_ID_REPORTES
import com.example.agileus.utils.Constantes.messageStadisticData
import com.example.agileus.utils.Constantes.fechaFinEstadisticas
import com.example.agileus.utils.Constantes.fechaIniEstadisticas
import com.example.agileus.utils.Constantes.idUsuarioEstadisticas
import com.example.agileus.utils.Constantes.id_broadcast
import retrofit2.Response
import java.time.ZonedDateTime

class ReporteMensajesDao {

    private var contador_mensajes_enviados:Int = 0
    private var contador_mensajes_recibidos:Int = 0
    private var contador_mensajes_leidos:Int=0
    private var contador_mensajes_totales:Int=0
    private var recibidos_broadcast:Int=0
    private var enviados_al_B:Int=0
    private lateinit var fecha_inicio: ZonedDateTime
    private lateinit var fecha_fin: ZonedDateTime
    lateinit var fecha_actual: ZonedDateTime

    var employeeList = ArrayList<Contacts>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun recuperardatosMensajes(idBusqueda: String): ArrayList<Estadisticas> {

        val listaRecycler= ArrayList<Estadisticas>()
        val messageSelectedStadistic = recoverUserMessageStadistic(idBusqueda, idUsuarioEstadisticas)

        contador_mensajes_enviados= messageSelectedStadistic.send
        contador_mensajes_recibidos=messageSelectedStadistic.received
        contador_mensajes_totales=messageSelectedStadistic.total
        contador_mensajes_leidos=messageSelectedStadistic.read
        enviados_al_B = messageSelectedStadistic.sendBroadcast
        recibidos_broadcast= messageSelectedStadistic.receivedBroadcast

        listaRecycler.add(Estadisticas("Enviados:",contador_mensajes_enviados.toString(),"Recibidos:",contador_mensajes_recibidos.toString(), R.drawable.ic_pie_chart))
        listaRecycler.add(Estadisticas("Enviados al Broadcast:",enviados_al_B.toString(),"Recibidos del Broadcast:",recibidos_broadcast.toString(), R.drawable.ic_bar_chart))

        return listaRecycler

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun recoverUserMessageStadistic(idBusqueda: String, searchName: String): UserMessageDetailReport {
        var lista: ArrayList<Conversation>
        var lista_B: ArrayList<DatosBroadCast>
        var messageDetail = UserMessageDetailReport()

        val id_usuario_actual = idBusqueda //Aquí se coloca el id del emisor deseado
        var contador_m_enviados= 0
        var contador_m_recibidos = 0
        var contador_m_leidos=0
        var contador_m_totales=0
        //val id_BroadCast = id_broadcast// id del Broadcast
        var contador_recibidos_B = 0
        var broadcastSize = 0

        try{
            val callRespuesta = InitialApplication.webServiceGlobalReportes.getDatosReporteMensajes(preferenciasGlobal.recuperarIdSesion(), idBusqueda)
            val ResponseMensajes: Response<ResponseConversation> = callRespuesta.execute()

            val callRespuestaBroadCast = InitialApplication.webServiceGlobalReportes.getDatosRespuestasBroadcast(preferenciasGlobal.recuperarIdSesion(), idBusqueda)
            val ResponseMensajesBroadCast: Response<BroadcastByID> = callRespuestaBroadCast.execute()

            //Fechas por día, mes, año, custom y default
            fecha_inicio = ZonedDateTime.parse(fechaIniEstadisticas)
            fecha_fin = ZonedDateTime.parse(fechaFinEstadisticas)
            if (idBusqueda == TEAM_ID_REPORTES){
                messageDetail = messageStadisticData[messageStadisticData.size-1]
            }else if (ResponseMensajes.isSuccessful || ResponseMensajesBroadCast.isSuccessful) {
                Log.d("messageORbroadcast", searchName)
                try {
                    val listaDS = ResponseMensajes.body()!!
                    lista = listaDS.data
                    Log.d("MensajesConsumidos:", lista.size.toString())
                    lista.forEach {

                        fecha_actual = ZonedDateTime.parse(it.fechaEnviado)
                        if((fecha_actual.isEqual(fecha_inicio) || fecha_actual.isAfter(fecha_inicio)) &&
                            (fecha_actual.isBefore(fecha_fin))) {
                            contador_m_totales += 1

                            if (id_usuario_actual == it.idemisor) {
                                contador_m_enviados += 1
                            }else{
                                contador_m_recibidos += 1
                            }
                            if (it.statusLeido == true) {
                                contador_m_leidos += 1
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
                    val objB = ResponseMensajesBroadCast.body()!!
                    lista_B = objB.data
                    broadcastSize = lista_B.size
                    Log.d("RMDao MeMensajesBroadcast", broadcastSize.toString())
                }catch (ex: java.lang.Exception){
                    Log.d("RMDao NoDATABroadcast", ex.toString())
                }

                messageDetail = UserMessageDetailReport(
                    idBusqueda,
                    searchName,
                    contador_m_enviados,
                    contador_m_recibidos,
                    contador_m_leidos,
                    contador_m_totales,
                    broadcastSize,
                    contador_recibidos_B
                )
            }else{
                Log.d("RMDao", "NoConection to Broadcast or Messages")
                messageDetail = UserMessageDetailReport(
                    idBusqueda,
                    searchName,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0
                )
            }
        }catch (ex: java.lang.Exception){
            Log.d("RMDao", "Empty/Denied DataMList")
            messageDetail = UserMessageDetailReport(
                idBusqueda,
                searchName,
                0,
                0,
                0,
                0,
                0,
                0
            )
        }

        return messageDetail
    }
    
@RequiresApi(Build.VERSION_CODES.O)
fun obtenerListaSubContactos(idUser:String): ArrayList<UserMessageDetailReport> {
    var stadisticEmployeesList = arrayListOf<UserMessageDetailReport>()

    stadisticEmployeesList.add(recoverUserMessageStadistic(idUser, preferenciasGlobal.recuperarNombreSesion()))

        try{
            val callRespuesta = InitialApplication.webServiceGlobalReportes.getListSubContacts( idUser)
            var ResponseDos:Response<EmployeeListByBossID> = callRespuesta.execute()

            if (ResponseDos.isSuccessful){
                try{
                    val listaConsumida = ResponseDos.body()!!
                    employeeList = listaConsumida.dataEmployees

                    //Obtencion de estadisticas de los empleados
                    if(employeeList.isNotEmpty()){
                        employeeList.forEach {
                            stadisticEmployeesList.add(recoverUserMessageStadistic(it.id, it.nombre))
                        }
                        stadisticEmployeesList.add(totalGroupEstadisticsBYBoss(stadisticEmployeesList))
                    }
                }catch (ex: java.lang.Exception){
                    Log.e("RMDao, NoLowLevelUsers", "Response "+ex.toString())

                }
            }else{
                Log.e("RMDao SubContactos", "Respuesta fallida:" + ResponseDos.code().toString())
            }
        }catch (ex:Exception){
            Log.e("SubContactos", "No SUBContacts / Error al iniciar servicio")
        }
        stadisticEmployeesList.forEach {
            Log.d("LSubConactsDetailm", "id: ${it.id}, Nombre: ${it.name}, " +
                    "send: ${it.send}, received: ${it.received}, read: ${it.read}, total: ${it.total}, " +
                    "sendB: ${it.sendBroadcast}, receivedB: ${it.receivedBroadcast}")
        }
        return stadisticEmployeesList
    }

    fun totalGroupEstadisticsBYBoss(dataValues: ArrayList<UserMessageDetailReport>): UserMessageDetailReport{
        var send        = 0
        var received    = 0
        var read        = 0
        var total       = 0
        var sendBroadcast       = 0
        var receivedBroadcast   = 0

        for (data in dataValues.slice(1..(dataValues.size - 1))){
            send        += data.send
            received    += data.received
            read        += data.read
            total       += data.total
            sendBroadcast       += data.sendBroadcast
            receivedBroadcast   += data.receivedBroadcast
        }

        return UserMessageDetailReport(
            TEAM_ID_REPORTES,
            "Mi equipo",
            send,
            received,
            read,
            total,
            sendBroadcast,
            receivedBroadcast
        )

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
    fun obtenerEnviadosBroadcast():String{
        return enviados_al_B.toString()
    }

    fun obtenerRecibidosBroadcast():String{
        return recibidos_broadcast.toString()
    }


}