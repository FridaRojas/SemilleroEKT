package com.example.agileus.webservices.dao

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.agileus.R
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.DatosTareas
import com.example.agileus.models.Estadisticas
import com.example.agileus.config.MySharedPreferences
import com.example.agileus.config.MySharedPreferences.reportesGlobales.fechaFinEstadisticas
import com.example.agileus.config.MySharedPreferences.reportesGlobales.fechaIniCustomEstadisticas
import com.example.agileus.config.MySharedPreferences.reportesGlobales.fechaIniEstadisticas
import com.example.agileus.config.MySharedPreferences.reportesGlobales.idUsuarioEstadisticas
import com.example.agileus.config.MySharedPreferences.reportesGlobales.opcionFiltro
import com.example.agileus.models.*
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
    lateinit var fecha_inicio: ZonedDateTime
    lateinit var fecha_actual: ZonedDateTime
    lateinit var fecha_fin: ZonedDateTime
    lateinit var fechaFinTer: ZonedDateTime
    lateinit var fechaFinTerR: ZonedDateTime

    var employeeList = ArrayList<Contacts>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun recuperardatosTareas(): ArrayList<Estadisticas> {

        val callRespuesta = InitialApplication.webServiceGlobalReportes.getDatosReporteTareas()
        val ResponseTareas: Response<ArrayList<DatosTareas>> = callRespuesta.execute()

        var listaRecycler= ArrayList<Estadisticas>()
        val lista: ArrayList<DatosTareas>

        when (opcionFiltro) {

            0 -> {
                //Desde la fecha default hasta ahora
                fecha_inicio = ZonedDateTime.parse(fechaIniEstadisticas)
                fecha_fin =ZonedDateTime.now()
            }
            else -> {
                //Fechas por día, mes, año y custom
                fecha_inicio = ZonedDateTime.parse(fechaIniEstadisticas)
                fecha_fin = ZonedDateTime.parse(fechaFinEstadisticas)

            }

        }

        if (ResponseTareas.isSuccessful) {
            lista = ResponseTareas.body()!!

            val id_receptor = "RECEPT1"                             //TODO id receptor real
                //lista[0].id_receptor//Aquí se coloca el id del usuario a revisar

            var contador_t_leidas=0
            var contador_t_totales=0
            var contador_t_terminadas= 0
            var contador_t_pendientes = 0
            var contCancelado = 0
            var contIniciada = 0
            var contRevision = 0
            var contTareasaTiempo = 0
            var contTareasFueraTiempo = 0

            /*
            Pendientes
            Cancelado
            Iniciada
            Revision
            Terminada
             */
            lista.forEach {

                fecha_actual = ZonedDateTime.parse(it.fecha_ini)

                if((fecha_actual.isEqual(fecha_inicio) || fecha_actual.isAfter(fecha_inicio)) &&
                    (fecha_actual.isBefore(fecha_fin))) {

                    contador_t_totales = contador_t_totales + 1

                    if (id_receptor == it.idReceptor) {

                        if (it.leido) {
                            contador_t_leidas = contador_t_leidas + 1

                            if (it.status.equals("Terminada")) {
                                contador_t_terminadas = contador_t_terminadas + 1
                            }

                            if (it.status.equals("Pendiente")) {
                                contador_t_pendientes = contador_t_pendientes + 1
                            }
                            if (it.status.equals("Iniciada")) {
                                contIniciada = +1
                            }
                            if (it.status.equals("Revision")) {
                                contRevision = +1
                            }
                        }
                        else {  //Cancel
                            if (it.status.equals("Cancelado"))
                                contCancelado = +1
                            }
                        }

                    fechaFinTer = ZonedDateTime.parse(it.fecha_fin)
                    fechaFinTerR = ZonedDateTime.parse(it.fecha_finR)

                    if (fechaFinTer.isBefore(fechaFinTerR) || fechaFinTer.isEqual(fechaFinTerR)){
                        contTareasaTiempo += 1
                    }else{
                        contTareasFueraTiempo = contTareasFueraTiempo + 1
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

        }

            listaRecycler.add(Estadisticas("Terminadas",contador_tareas_terminadas.toString(),"Pendientes",contador_tareas_pendientes.toString(), R.drawable.ic_pie_chart))
            listaRecycler.add(Estadisticas("Tareas culminadas a tiempo:",contador_tareas_aTiempo.toString(),"Tareas culminadas fuera de tiempo:",contador_tareas_fueraTiempo.toString(), R.drawable.ic_bar_chart))

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

        }
        return employeeList
    }


}
