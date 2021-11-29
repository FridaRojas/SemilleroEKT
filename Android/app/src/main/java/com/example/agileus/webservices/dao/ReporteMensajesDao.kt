package com.example.agileus.webservices.dao

import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.util.Log
import com.example.agileus.R
import com.example.agileus.config.InitialApplication
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


    fun recuperardatosMensajes(): ArrayList<Estadisticas> {

        val callRespuesta = InitialApplication.webServiceGlobalReportes.getDatosReporteMensajes()
        val ResponseMensajes: Response<ArrayList<DatosMensajes>> = callRespuesta.execute()

        val listaRecycler= ArrayList<Estadisticas>()
        val lista: ArrayList<DatosMensajes>




        val actualCal= Calendar.getInstance()
        var dt = Date(actualCal.get(Calendar.YEAR)-1900, actualCal.get(Calendar.MONTH) , actualCal.get(Calendar.DAY_OF_MONTH))
        val sdf = SimpleDateFormat("dd/MM/y", Locale.US)
        val currentDate = sdf.format(dt)



        if (ResponseMensajes.isSuccessful) {
            lista = ResponseMensajes.body()!!
            val id_emisor = lista[0].idemisor //Aquí se coloca el id del emisor deseado
            MySharedPreferences.reportesGlobales.idUsuario = id_emisor  // ID De la sesion TO recuperar del inicio de sesion
            MySharedPreferences.reportesGlobales.idUsuarioEstadisticas = id_emisor  // ID De la sesion TO recuperar del inicio de sesion
            MySharedPreferences.fechaFinEstadisticas = currentDate
            MySharedPreferences.fechaInicioEstadisticas = "1970/01/01"
            Log.d("Response", "user: ${MySharedPreferences.idUsuario}, userEST: ${MySharedPreferences.idUsuarioEstadisticas}, ini: ${MySharedPreferences.fechaInicioEstadisticas}, fin: ${MySharedPreferences.fechaFinEstadisticas}")
            fecha_anterior = ZonedDateTime.parse(lista[0].fechaEnviado) // primera fecha para comparar

            lista.forEach {

                contador_mensajes_totales = contador_mensajes_totales + 1

                if(id_emisor==it.idemisor){
                    contador_mensajes_enviados = contador_mensajes_enviados + 1

                     fecha_actual = ZonedDateTime.parse(it.fechaEnviado)
                     diferencia_minutos = ChronoUnit.MINUTES.between(fecha_anterior, fecha_actual)

                     suma_tiempos = suma_tiempos + diferencia_minutos.toInt()

                    Log.d("mensaje","suma tiempos: ${suma_tiempos}")

                    fecha_anterior = fecha_actual

                    temporal=temporal+1
                }
                else{

                    contador_mensajes_recibidos = contador_mensajes_recibidos + 1

                    if(it.statusLeido){
                        contador_mensajes_leidos = contador_mensajes_leidos + 1
                    }

                }

            }

            if(temporal==0)
            promedio_tiempo_respuesta = "Sin tiempo de respuesta."
            else
            promedio_tiempo_respuesta = "${((suma_tiempos)/(temporal - 1))} minutos."
            //Log.d("mensaje","suma tiempos: ${suma_tiempos}")

                listaRecycler.add(Estadisticas("Enviados:",contador_mensajes_enviados.toString(),"Recibidos:",contador_mensajes_recibidos.toString(), R.drawable.ic_pie_chart))
                listaRecycler.add(Estadisticas("Peticiones al Broadcast",contador_mensajes_enviados.toString(),"Respuestas del Broadcast",contador_mensajes_recibidos.toString(), R.drawable.ic_bar_chart))
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
                Log.e("ERROR SubCOntactos", ResponseDos.code().toString())
            }

        }catch (ex:Exception){

        }
        return employeeList
    }


}