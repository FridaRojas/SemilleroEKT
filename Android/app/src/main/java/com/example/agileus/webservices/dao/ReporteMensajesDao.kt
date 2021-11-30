package com.example.agileus.webservices.dao

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.agileus.R
import com.example.agileus.config.InitialApplication
import com.example.agileus.config.MySharedPreferences.reportesGlobales.idUsuarioEstadisticas
import com.example.agileus.models.Contacts
import com.example.agileus.models.Conversation
import com.example.agileus.models.EmployeeListByBossID
import com.example.agileus.models.Estadisticas
import com.example.agileus.config.MySharedPreferences
import com.example.agileus.models.*
import retrofit2.Response
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

class ReporteMensajesDao {


    private var contador_mensajes_enviados:Int = 0
    private var contador_mensajes_recibidos:Int = 0
    private var contador_mensajes_leidos:Int=0
    private var contador_mensajes_totales:Int=0
    private var diferencia_minutos: Long = 0
    private var fecha_actual: ZonedDateTime? = null
    private var fecha_anterior: ZonedDateTime? = null
    private var suma_tiempos:Int=0
    private var temporal:Int=0
    private lateinit var promedio_tiempo_respuesta:String

    var employeeList = ArrayList<Contacts>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun recuperardatosMensajes(): ArrayList<Estadisticas> {

        val callRespuesta = InitialApplication.webServiceGlobalReportes.getDatosReporteMensajes()
        val ResponseMensajes: Response<ArrayList<Conversation>> = callRespuesta.execute()

        val listaRecycler= ArrayList<Estadisticas>()
        val lista: ArrayList<Conversation>





        val actualCal= Calendar.getInstance()
        var dt = Date(actualCal.get(Calendar.YEAR)-1900, actualCal.get(Calendar.MONTH) , actualCal.get(Calendar.DAY_OF_MONTH))
        val sdf = SimpleDateFormat("dd/MM/y", Locale.US)
        val currentDate = sdf.format(dt)



        if (ResponseMensajes.isSuccessful) {
            lista = ResponseMensajes.body()!!
            val id_emisor = idUsuarioEstadisticas//lista[0].idemisor //Aquí se coloca el id del emisor deseado

            var contador_m_enviados= 0
            var contador_m_recibidos = 0
            var contador_m_leidos=0
            var contador_m_totales=0
            var tiempo_p_respuesta=""
            temporal=0
            suma_tiempos=0
            promedio_tiempo_respuesta=""

            // primera fecha para comparar TODO valor de GLOBAL fecha inicio
            fecha_anterior = ZonedDateTime.parse(lista[0].fechaEnviado)  //1970-01-01T00:00:00.000+00:00"
            lista.forEach {

                contador_m_totales = contador_m_totales + 1

                if(id_emisor==it.idemisor){
                    contador_m_enviados = contador_m_enviados + 1

                     fecha_actual = ZonedDateTime.parse(it.fechaEnviado)
                     //var fecha_anterior=fecha_anterior
                     diferencia_minutos = ChronoUnit.MINUTES.between(fecha_anterior, fecha_actual)

                     if(fecha_anterior!!.isBefore(fecha_actual) || fecha_anterior!!.isEqual(fecha_actual)){
                    Log.d("mensaje","fecha 1: ${fecha_anterior.toString()}" +
                            "   fecha 2: ${fecha_actual.toString()}. ")
                      }

                     suma_tiempos = suma_tiempos + diferencia_minutos.toInt()

                    Log.d("mensaje","suma tiempos: ${suma_tiempos}")

                    fecha_anterior = fecha_actual

                    temporal=temporal+1
                }
                else{

                    contador_m_recibidos = contador_m_recibidos + 1

                    if(it.statusLeido=="true"){
                        contador_m_leidos = contador_m_leidos + 1
                    }

                }


            }

            if(temporal==0)
            tiempo_p_respuesta = "Sin tiempo de respuesta."
            else
            tiempo_p_respuesta = "${((suma_tiempos)/(temporal - 1))} minutos."
            //Log.d("mensaje","suma tiempos: ${suma_tiempos}")

            contador_mensajes_enviados=contador_m_enviados
            contador_mensajes_recibidos=contador_m_leidos
            contador_mensajes_totales=contador_m_totales
            contador_mensajes_leidos=contador_m_leidos
            promedio_tiempo_respuesta=tiempo_p_respuesta

                listaRecycler.add(Estadisticas("Enviados:",contador_mensajes_enviados.toString(),"Recibidos:",contador_mensajes_recibidos.toString(), R.drawable.ic_pie_chart))
                listaRecycler.add(Estadisticas("Promedio de respuesta del Broadcast:","","",promedio_tiempo_respuesta, R.drawable.ic_bar_chart))
        }

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