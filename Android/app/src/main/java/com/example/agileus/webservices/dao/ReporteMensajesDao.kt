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
    lateinit var fecha_inicio: ZonedDateTime
    lateinit var fecha_fin: ZonedDateTime
    lateinit var fecha_actual: ZonedDateTime
    var employeeList = ArrayList<Contacts>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun recuperardatosMensajes(): ArrayList<Estadisticas> {

        val callRespuesta = InitialApplication.webServiceGlobalReportes.getDatosReporteMensajes()
        val ResponseMensajes: Response<ArrayList<Conversation>> = callRespuesta.execute()

        val callRespuestaBroadCast = InitialApplication.webServiceGlobalReportes.getDatosRespuestasBroadcast()
        val ResponseMensajesBroadCast: Response<ArrayList<DatosBroadCast>> = callRespuestaBroadCast.execute()

        val listaRecycler= ArrayList<Estadisticas>()
        var lista: ArrayList<Conversation>
        var lista_B: ArrayList<DatosBroadCast>

        //Fechas por día, mes, año, custom y default
        fecha_inicio = ZonedDateTime.parse(fechaIniEstadisticas)
        fecha_fin = ZonedDateTime.parse(fechaFinEstadisticas)

        if (ResponseMensajes.isSuccessful) {
            lista = ResponseMensajes.body()!!
            val id_usuario_actual = idUsuarioEstadisticas //Aquí se coloca el id del emisor deseado
            var contador_m_enviados= 0
            var contador_m_recibidos = 0
            var contador_m_leidos=0
            var contador_m_totales=0
            //val id_BroadCast = id_broadcast// id del Broadcast
            var contador_recibidos_B = 0

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

            contador_mensajes_enviados=contador_m_enviados
            contador_mensajes_recibidos=contador_m_leidos
            contador_mensajes_totales=contador_m_totales
            contador_mensajes_leidos=contador_m_leidos
            recibidos_broadcast=contador_recibidos_B
        }

        if(ResponseMensajesBroadCast.isSuccessful){
            lista_B = ResponseMensajesBroadCast.body()!!
            Log.d("link:",lista_B.toString())
            enviados_al_B = lista_B.size
        }

        listaRecycler.add(Estadisticas("Enviados:",contador_mensajes_enviados.toString(),"Recibidos:",contador_mensajes_recibidos.toString(), R.drawable.ic_pie_chart))
        listaRecycler.add(Estadisticas("Enviados al Broadcast:",enviados_al_B.toString(),"Recibidos del Broadcast:",recibidos_broadcast.toString(), R.drawable.ic_bar_chart))

        return listaRecycler

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

    fun obtenerListaSubContactos(idUser:String): ArrayList<Contacts> {
        try{
            //val callRespuesta = InitialApplication.webServiceGlobalReportes.getListSubContacts(idUser)
            val callRespuesta = InitialApplication.webServiceGlobalReportes.getListSubContacts()
            var ResponseDos:Response<EmployeeListByBossID> = callRespuesta.execute()

            if (ResponseDos.isSuccessful){
                val listaConsumida = ResponseDos.body()!!
                employeeList = listaConsumida.dataEmployees
            }else{
                Log.e("ERROR SubContactos", ResponseDos.code().toString())
            }

        }catch (ex:Exception){

        }
        return employeeList
    }


}