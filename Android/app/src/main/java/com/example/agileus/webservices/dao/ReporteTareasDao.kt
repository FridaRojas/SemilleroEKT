package com.example.agileus.webservices.dao

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.agileus.R
import com.example.agileus.config.InitialApplication
import com.example.agileus.config.InitialApplication.Companion.preferenciasGlobal
import com.example.agileus.models.*
import com.example.agileus.utils.Constantes.MIN_DATE_RANGE
import com.example.agileus.utils.Constantes.TEAM_ID_REPORTES
import com.example.agileus.utils.Constantes.taskStadisticData
import com.example.agileus.utils.Constantes.fechaFinEstadisticas
import com.example.agileus.utils.Constantes.fechaIniEstadisticas
import com.example.agileus.utils.Constantes.idUsuarioEstadisticas
import retrofit2.Response
import java.time.ZonedDateTime

class ReporteTareasDao {

    private var contador_tareas_terminadas:Int = 0
    private var contador_tareas_pendientes:Int = 0
    private var contador_tareas_revision:Int = 0
    private var contador_tareas_iniciada:Int = 0
    private var contador_tareas_aTiempo:Int = 0
    private var contador_tareas_fueraTiempo:Int = 0
    private var contador_tareas_leidas:Int=0
    private var contador_tareas_totales:Int=0

    private lateinit var rangoIniFecha: ZonedDateTime
    private lateinit var rangoFinFecha: ZonedDateTime

    lateinit var fechaFin: ZonedDateTime
    lateinit var fechaFinR: ZonedDateTime

    var employeeList = ArrayList<Contacts>()
    var lista = ArrayList<DatosTareas>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun recuperardatosTareas(idBusqueda: String): ArrayList<Estadisticas> {

        var listaRecycler= ArrayList<Estadisticas>()
        val taskSelectedDetail = recoverUserTaskDetails(idBusqueda, idUsuarioEstadisticas)

        contador_tareas_terminadas  = taskSelectedDetail.finished
        contador_tareas_pendientes  = taskSelectedDetail.pendings
        contador_tareas_revision    = taskSelectedDetail.revision
        contador_tareas_iniciada    = taskSelectedDetail.started
        contador_tareas_aTiempo     = taskSelectedDetail.onTime
        contador_tareas_fueraTiempo = taskSelectedDetail.outTime
        contador_tareas_totales     = taskSelectedDetail.totals

        listaRecycler.add(Estadisticas("Terminadas",contador_tareas_terminadas.toString(),
            "Pendientes",contador_tareas_pendientes.toString(), R.drawable.ic_pie_chart))
        listaRecycler.add(Estadisticas("Tareas culminadas a tiempo:","","",
            contador_tareas_aTiempo.toString(), R.drawable.ic_bar_chart))

        return listaRecycler
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun recoverUserTaskDetails(idBusqueda: String, nombreBusqueda: String):UserTaskDetailReport{
        var taskDetail= UserTaskDetailReport()
        var contador_t_totales=0
        var contador_t_terminadas= 0
        var contador_t_pendientes = 0
        var contCancelado = 0
        var contIniciada = 0
        var contRevision = 0
        var contTareasaTiempo = 0
        var contTareasFueraTiempo = 0

        Log.d("RTDao", nombreBusqueda)
        if (idBusqueda == TEAM_ID_REPORTES){
            try {
                taskDetail = taskStadisticData[taskStadisticData.size-1]
            }catch (ex:Exception){
                Log.e("RTDao", ex.toString())
            }
        }else{
            try {
                val callRespuesta = InitialApplication.webServiceGlobalReportes.getDatosReporteTareas(preferenciasGlobal.recuperarIdSesion(), idBusqueda)
                val ResponseTareas: Response<TaskListByID> = callRespuesta.execute()

                if (ResponseTareas.isSuccessful) {
                    try {
                        val listaDs = ResponseTareas.body()!!
                        //Log.e("RTDAO1", ResponseTareas.message().toString())
                        //Log.e("RTDAO2", ResponseTareas.errorBody().toString())
                        //Log.e("RTDAO3", ResponseTareas.code().toString())
                        //Log.e("RTDAO4", listaDs.data.toString())
                        lista = listaDs.data
                        Log.d("TareasConsumidos:", lista.size.toString())
                        rangoIniFecha = ZonedDateTime.parse(fechaIniEstadisticas)
                        rangoFinFecha = ZonedDateTime.parse(fechaFinEstadisticas)

                        lista.forEach {
                            val dateIni = ZonedDateTime.parse(it.fecha_ini)
                            if ((dateIni.isAfter(rangoIniFecha) || dateIni.isEqual(rangoIniFecha)) &&
                                (dateIni.isBefore(rangoFinFecha) || dateIni.isEqual(rangoFinFecha)) ){
                                //Log.e("RangoIN", "$rangoIniFecha / $dateIni / $rangoFinFecha")      //Lectura en rango de tareas
                                //if(id_receptor==it.idReceptor) {
                                contador_t_totales += 1

                                if (it.estatus.lowercase().equals("terminada")) {
                                    contador_t_terminadas += 1
                                } else if(it.estatus.lowercase().equals("pendiente")) {
                                    contador_t_pendientes += 1
                                } else if(it.estatus.lowercase().equals("iniciada")){
                                    contIniciada += 1
                                } else if(it.estatus.lowercase().equals("revision")){
                                    contRevision += 1
                                }else if(it.estatus.lowercase().equals("cancelado")){  //Cancelado
                                    contCancelado += 1
                                }else{
                                    Log.e("taskStatusFormatERROR", "find: ${it.estatus}")
                                }

                                try {
                                    fechaFin = ZonedDateTime.parse(it.fecha_fin)
                                    fechaFinR = ZonedDateTime.parse(it.fecha_finR)
                                    if (fechaFinR.isBefore(fechaFin) || fechaFinR.isEqual(fechaFin)){
                                        contTareasaTiempo += 1
                                    }else{
                                        contTareasFueraTiempo += 1
                                    }
                                }catch (ex:Exception){
                                    fechaFin = ZonedDateTime.parse(MIN_DATE_RANGE)
                                    fechaFinR = ZonedDateTime.parse(MIN_DATE_RANGE)
                                }
                            }
                        }

                    }catch (ex:java.lang.Exception){
                        Log.d("RTDao", " no Data/access ")
                    }

                    taskDetail = UserTaskDetailReport(
                        idBusqueda,
                        nombreBusqueda,
                        contador_t_totales,
                        contador_t_terminadas,
                        0,
                        0,
                        0,
                        contador_t_pendientes,
                        contCancelado,
                        contIniciada,
                        contRevision,
                        contTareasaTiempo,
                        contTareasFueraTiempo
                    )
                    //Log.d("DetailsTASK", "D: ${taskDetail}")
                }else{
                    taskDetail = UserTaskDetailReport(
                        idBusqueda,
                        nombreBusqueda,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0
                    )
                }
            }catch (ex: java.lang.Exception){
                Log.d("RTDao Error al recuperar", "$nombreBusqueda: $ex")
                taskDetail = UserTaskDetailReport(
                    idBusqueda,
                    nombreBusqueda,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0,
                    0
                )
            }
        }

        return taskDetail
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun obtenerListaSubContactos(idUser:String): ArrayList<UserTaskDetailReport> {
        var stadisticEmployeesList = arrayListOf<UserTaskDetailReport>()
        stadisticEmployeesList.add(recoverUserTaskDetails(idUser, preferenciasGlobal.recuperarNombreSesion()))
        try{
            val callRespuesta = InitialApplication.webServiceGlobalReportes.getListSubContacts(idUser)
            var ResponseDos:Response<EmployeeListByBossID> = callRespuesta.execute()

            if (ResponseDos.isSuccessful){
                try {
                    val listaConsumida = ResponseDos.body()!!
                    employeeList = listaConsumida.dataEmployees

                    //Obtencion de estadisticas de los empleados
                    if(employeeList.isNotEmpty()){
                        employeeList.forEach {
                            stadisticEmployeesList.add(recoverUserTaskDetails(it.id, it.nombre))
                        }
                        stadisticEmployeesList.add(totalGroupEstadisticsBYBoss(stadisticEmployeesList))
                    }

                    Log.d("ListaSubConactsSIZE", "SIZE: ${stadisticEmployeesList.size}")
                }catch (ex: java.lang.Exception){
                    Log.e("RTDao, NoLowLevelUsers", "Response "+ex.toString())

                }
            }else{
                Log.e("ERROR SubContactosT", "Respuesta fallida:" + ResponseDos.code().toString())
            }

        }catch (ex:Exception){
            Log.e("ERROR SubContactosT", "Error al iniciar servicio")
        }
        stadisticEmployeesList.forEach {
            Log.d("LSubContactsDetailT", "id: ${it.id}, Nombre: ${it.name}, totals: ${it.totals}," +
                    " finished: ${it.finished}, pendings: ${it.pendings}, canceled: ${it.canceled}, " +
                    " started: ${it.started}, revision: ${it.revision}, onTime: ${it.onTime}," +
                    " outTime: ${it.outTime},")
        }
        return stadisticEmployeesList
    }

    fun totalGroupEstadisticsBYBoss(dataValues: ArrayList<UserTaskDetailReport>): UserTaskDetailReport{
        var totals      = 0
        var finished    = 0
        var lowPriority = 0
        var mediumPriority  = 0
        var highPriority    = 0
        var pendings    = 0
        var canceled    = 0
        var started     = 0
        var revision    = 0
        var onTime      = 0
        var outTime     = 0

        for (data in dataValues.slice(1..(dataValues.size - 1))){
            totals      += data.totals
            finished    += data.finished
            lowPriority += data.lowPriority
            mediumPriority += data.mediumPriority
            highPriority    += data.highPriority
            pendings    += data.pendings
            canceled    += data.canceled
            started     += data.started
            revision    += data.revision
            outTime     += data.outTime
            onTime      += data.onTime
        }
        return UserTaskDetailReport(
            TEAM_ID_REPORTES,
            "Mi equipo",
            totals,
            finished,
            lowPriority,
            mediumPriority,
            highPriority,
            pendings,
            canceled,
            started,
            revision,
            outTime,
            onTime
        )

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

}
