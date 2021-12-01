package com.example.agileus.webservices.dao
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.agileus.R
import com.example.agileus.config.InitialApplication
import com.example.agileus.config.MySharedPreferences.reportesGlobales.fechaEstadisticas
import com.example.agileus.config.MySharedPreferences.reportesGlobales.fechaIniCustomEstadisticas
import com.example.agileus.config.MySharedPreferences.reportesGlobales.idUsuarioEstadisticas
import com.example.agileus.models.Contacts
import com.example.agileus.models.Conversation
import com.example.agileus.models.EmployeeListByBossID
import com.example.agileus.models.Estadisticas
import retrofit2.Response
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class ReporteMensajesDao {


    private var contador_mensajes_enviados:Int = 0
    private var contador_mensajes_recibidos:Int = 0
    private var contador_mensajes_leidos:Int=0
    private var contador_mensajes_totales:Int=0

    var employeeList = ArrayList<Contacts>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun recuperardatosMensajes(): ArrayList<Estadisticas> {

        val callRespuesta = InitialApplication.webServiceGlobalReportes.getDatosReporteMensajes()
        val ResponseMensajes: Response<ArrayList<Conversation>> = callRespuesta.execute()

        val callRespuestaBroadCast = InitialApplication.webServiceGlobalReportes.getDatosRespuestasBroadcast()
        val ResponseMensajesBroadCast: Response<ArrayList<Conversation>> = callRespuestaBroadCast.execute()


        val listaRecycler= ArrayList<Estadisticas>()
        var lista: ArrayList<Conversation>

        if (ResponseMensajes.isSuccessful) {
            lista = ResponseMensajes.body()!!
            val id_emisor = idUsuarioEstadisticas//lista[0].idemisor //Aquí se coloca el id del emisor deseado

            var contador_m_enviados= 0
            var contador_m_recibidos = 0
            var contador_m_leidos=0
            var contador_m_totales=0

            lista.forEach {

                contador_m_totales = contador_m_totales + 1

                if(id_emisor==it.idemisor){
                    contador_m_enviados = contador_m_enviados + 1
                }
                else{
                    contador_m_recibidos = contador_m_recibidos + 1

                    if(it.statusLeido=="true"){
                        contador_m_leidos = contador_m_leidos + 1
                    }

                }
            }

            contador_mensajes_enviados=contador_m_enviados
            contador_mensajes_recibidos=contador_m_leidos
            contador_mensajes_totales=contador_m_totales
            contador_mensajes_leidos=contador_m_leidos

                listaRecycler.add(Estadisticas("Enviados:",contador_mensajes_enviados.toString(),"Recibidos:",contador_mensajes_recibidos.toString(), R.drawable.ic_pie_chart))

        }

        if (ResponseMensajesBroadCast.isSuccessful) {
            lista = ResponseMensajesBroadCast.body()!!

            val id_BroadCast = idUsuarioEstadisticas // id del Broadcast
            var contador_enviados_B= 0
            var contador_recibidos_B = 0

            lista.forEach {

                if(id_BroadCast ==it.idemisor){
                    contador_enviados_B += 1
                }
                else{
                    contador_recibidos_B += 1
                }
            }

            listaRecycler.add(Estadisticas("Enviados al Broadcast:",contador_recibidos_B.toString(),"Recibidos del Broadcast:",contador_enviados_B.toString(), R.drawable.ic_bar_chart))

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