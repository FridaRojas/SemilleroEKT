package com.example.agileus.webservices.dao

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.agileus.R
import com.example.agileus.config.InitialApplication
import com.example.agileus.config.MySharedPreferences
import com.example.agileus.config.MySharedPreferences.reportesGlobales.idUsuarioEstadisticas
import com.example.agileus.models.*
import com.example.agileus.ui.HomeActivity
import retrofit2.Response
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class ReporteTareasDao {

    private var contador_tareas_terminadas:Int = 0
    private var contador_tareas_pendientes:Int = 0
    private var contador_tareas_revision:Int = 0
    private var contador_tareas_iniciada:Int = 0
    private var contador_tareas_aTiempo:Int = 0
    private var contador_tareas_fueraTiempo:Int = 0
    private var contador_tareas_leidas:Int=0
    private var contador_tareas_totales:Int=0

    private var fecha_anterior: ZonedDateTime? = null
    private lateinit var rangoIniFecha: ZonedDateTime
    private lateinit var rangoFinFecha: ZonedDateTime

    lateinit var fechaFin: ZonedDateTime
    lateinit var fechaFinR: ZonedDateTime

    var employeeList = ArrayList<Contacts>()
    var lista = ArrayList<DatosTareas>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun recuperardatosTareas(): ArrayList<Estadisticas> {

        val callRespuesta = InitialApplication.webServiceGlobalReportes.getDatosReporteTareas()
        val ResponseTareas: Response<TaskListByID> = callRespuesta.execute()
        var listaRecycler= ArrayList<Estadisticas>()

        if (ResponseTareas.isSuccessful) {
            val listaDs = ResponseTareas.body()!!
            lista = listaDs.data
            Log.e("COMSUMO", lista.size.toString())

            val id_receptor = "RECEPT1"                             //TODO id receptor real
                //lista[0].id_receptor//Aquí se coloca el id del usuario a revisar

            var contador_t_leidas=0
            var contador_t_totales=0

            var contador_t_terminadas= 0
            //Parametros de tareas
            var contador_t_pendientes = 0
            var contCancelado = 0
            var contIniciada = 0
            var contRevision = 0

            var contTareasaTiempo = 0
            var contTareasFueraTiempo = 0

            fecha_anterior = ZonedDateTime.parse(lista[0].fecha_ini) // primera fecha para comparar
            rangoIniFecha = ZonedDateTime.parse(MySharedPreferences.fechaIniEstadisticas) // primera fecha para comparar
            rangoFinFecha = ZonedDateTime.parse(MySharedPreferences.fechaFinEstadisticas) // segunda fecha para comparar

            Log.d("Rango", "ini: $fecha_anterior, fin:$rangoFinFecha")
            lista.forEach {
                val dateIni = ZonedDateTime.parse(it.fecha_ini)
                if ((dateIni.isAfter(rangoIniFecha) || dateIni.isEqual(rangoIniFecha)) &&
                    (dateIni.isBefore(rangoFinFecha) || dateIni.isEqual(rangoFinFecha)) ){

                    Log.e("RangoIN", "$rangoIniFecha / $dateIni / $rangoFinFecha")

                    //if(id_receptor==it.idReceptor) {
                    if(true) {
                        contador_t_totales = contador_t_totales + 1

                        /*
                        if (it.leido) {
                            contador_t_leidas = contador_t_leidas + 1
                        }
                         */

                        if (it.estatus.lowercase().equals("terminada")) {
                            contador_t_terminadas = contador_t_terminadas + 1
                        } else{
                            if(it.estatus.lowercase().equals("pendiente")) {
                                contador_t_pendientes = contador_t_pendientes + 1
                            } else if(it.estatus.lowercase().equals("iniciada")){
                                contIniciada =+ 1
                            } else if(it.estatus.lowercase().equals("revision")){
                                contRevision =+ 1
                            }else{  //Cancelado
                                contCancelado =+ 1
                            }
                        }
                    }

                    try {
                        fechaFin = ZonedDateTime.parse(it.fecha_fin)
                        fechaFinR = ZonedDateTime.parse(it.fecha_finR)
                        if (fechaFinR.isBefore(fechaFin) || fechaFinR.isEqual(fechaFin)){
                            contTareasaTiempo += 1
                        }else{
                            contTareasFueraTiempo = contTareasFueraTiempo + 1
                        }
                    }catch (ex:Exception){
                        fechaFin = ZonedDateTime.parse("1971-01-01T00:00:00.000+00:00")
                        fechaFinR = ZonedDateTime.parse("1970-01-01T00:00:00.000+00:00")
                    }

                }

            }

            contador_tareas_terminadas=contador_t_terminadas
            contador_tareas_pendientes=contador_t_pendientes
            contador_tareas_revision=contRevision
            contador_tareas_iniciada=contIniciada
            contador_tareas_aTiempo=contTareasaTiempo
            contador_tareas_fueraTiempo=contTareasFueraTiempo
            contador_tareas_totales=contador_t_totales
            contador_tareas_leidas=contador_t_leidas

            listaRecycler.add(Estadisticas("Terminadas",contador_tareas_terminadas.toString(),"Pendientes",contador_tareas_pendientes.toString(), R.drawable.ic_pie_chart))
            listaRecycler.add(Estadisticas("Tareas culminadas a tiempo:","","",contTareasaTiempo.toString(), R.drawable.ic_bar_chart))
        }
        return listaRecycler
    }

    fun obtenerTareasTerminadas():String{
        return contador_tareas_terminadas.toString()
    }

    fun obtenerTareasPendientes():String{
        return contador_tareas_pendientes.toString()
    }

    fun obtenerTareasEnRevision():String{
        return contador_tareas_revision.toString()
    }

    fun obtenerTareasIniciadas():String{
        return contador_tareas_iniciada.toString()
    }

    fun obtenerTareasATiempo():String{
        return contador_tareas_aTiempo.toString()
    }

    fun obtenerTareasFueraTiempo():String{
        return contador_tareas_fueraTiempo.toString()
    }

    fun obtenerTareasTotales():String{
        return contador_tareas_totales.toString()
    }

    fun obtenerTareasLeidas():String{
        return contador_tareas_leidas.toString()
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
            Log.e("ERROR SubContactos", "")
        }
        return employeeList
    }


}
