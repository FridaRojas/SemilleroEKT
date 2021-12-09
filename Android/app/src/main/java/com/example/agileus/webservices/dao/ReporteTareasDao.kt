package com.example.agileus.webservices.dao

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.agileus.R
import com.example.agileus.config.InitialApplication
import com.example.agileus.config.MySharedPreferences
import com.example.agileus.models.*
import com.example.agileus.utils.Constantes.dataEmpleadoUsuario
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

    private var fecha_anterior: ZonedDateTime? = null
    private lateinit var rangoIniFecha: ZonedDateTime
    private lateinit var rangoFinFecha: ZonedDateTime

    lateinit var fechaFin: ZonedDateTime
    lateinit var fechaFinR: ZonedDateTime

    var employeeList = ArrayList<Contacts>()
    var lista = ArrayList<DatosTareas>()
    var stadisticEmployeesList = ArrayList<UserTaskDetailReport>()



    @RequiresApi(Build.VERSION_CODES.O)
    fun recuperardatosTareas(idBusqueda: String): ArrayList<Estadisticas> {

        val callRespuesta = InitialApplication.webServiceGlobalReportes.getDatosReporteTareas(idBusqueda)
        // val ResponseTareas: Response<TaskListByID> = callRespuesta.execute()
        var listaRecycler= ArrayList<Estadisticas>()

        val taskSelectedDetail = recoverUserTaskDetails(idBusqueda, idUsuarioEstadisticas)

        contador_tareas_terminadas=taskSelectedDetail.finished
        contador_tareas_pendientes=taskSelectedDetail.pendings
        contador_tareas_revision=taskSelectedDetail.revision
        contador_tareas_iniciada=taskSelectedDetail.started
        contador_tareas_aTiempo=taskSelectedDetail.onTime
        contador_tareas_fueraTiempo=taskSelectedDetail.outTime
        contador_tareas_totales=taskSelectedDetail.totals

        listaRecycler.add(Estadisticas("Terminadas",contador_tareas_terminadas.toString(),"Pendientes",contador_tareas_pendientes.toString(), R.drawable.ic_pie_chart))
        listaRecycler.add(Estadisticas("Tareas culminadas a tiempo:","","",contador_tareas_aTiempo.toString(), R.drawable.ic_bar_chart))

        return listaRecycler
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun recoverUserTaskDetails(idBusqueda: String, nombreBusqueda: String):UserTaskDetailReport{

        val callRespuesta = InitialApplication.webServiceGlobalReportes.getDatosReporteTareas(idBusqueda)
        val ResponseTareas: Response<TaskListByID> = callRespuesta.execute()
        var taskDetail= UserTaskDetailReport()

        if (idBusqueda == "TEAM_ID_CREATED_BY_MOD_REPORT"){
            try {
                taskDetail = dataEmpleadoUsuario[dataEmpleadoUsuario.size-1]
            }catch (ex:Exception){
                Log.e("RTDao", ex.toString())
            }

        }else if (ResponseTareas.isSuccessful) {
            val listaDs = ResponseTareas.body()!!
            lista = listaDs.data
            Log.e("CONSUMO", lista.size.toString())

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
            rangoIniFecha = ZonedDateTime.parse(fechaIniEstadisticas) // primera fecha para comparar
            rangoFinFecha = ZonedDateTime.parse(fechaFinEstadisticas) // segunda fecha para comparar

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
                        } else if(it.estatus.lowercase().equals("pendiente")) {
                            contador_t_pendientes = contador_t_pendientes + 1
                        } else if(it.estatus.lowercase().equals("iniciada")){
                            contIniciada =+ 1
                        } else if(it.estatus.lowercase().equals("revision")){
                            contRevision =+ 1
                        }else if(it.estatus.lowercase().equals("cancelado")){  //Cancelado
                            contCancelado =+ 1
                        }else{
                            Log.e("taskStatusFormatERROR", "find: ${it.estatus}")
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
            Log.d("DetailsTASK", "D: ${taskDetail}")
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

        Log.d("DetailsTASK2", taskDetail.toString())
        return taskDetail
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun obtenerListaSubContactos(idUser:String): ArrayList<UserTaskDetailReport> {
        try{
            //val callRespuesta = InitialApplication.webServiceGlobalReportes.getListSubContacts(idUser)
            val callRespuesta = InitialApplication.webServiceGlobalReportes.getListSubContacts( idUser)
            var ResponseDos:Response<EmployeeListByBossID> = callRespuesta.execute()

            if (ResponseDos.isSuccessful){
                val listaConsumida = ResponseDos.body()!!
                employeeList = listaConsumida.dataEmployees

                stadisticEmployeesList.add(recoverUserTaskDetails(idUser, "Mi información"))

                //Obtencion de estadisticas de los empleados
                if(employeeList.isNotEmpty()){
                    employeeList.forEach {
                        stadisticEmployeesList.add(recoverUserTaskDetails(it.id, it.nombre))
                        Log.d("ListaSubConacts", "id: ${it.id}")
                        Log.d("ListaSubConacts", "Nombre: ${it.nombre}")
                    }
                    stadisticEmployeesList.add(totalGroupEstadisticsBYBoss(stadisticEmployeesList))
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

    fun totalGroupEstadisticsBYBoss(dataValues: ArrayList<UserTaskDetailReport>): UserTaskDetailReport{
        var totals=0
        var finished = 0
        var lowPriority = 0
        var mediumPriority = 0
        var highPriority = 0
        var pendings = 0
        var canceled: Int = 0
        var started: Int = 0
        var revision: Int = 0
        var onTime: Int = 0
        var outTime: Int = 0

        for (data in dataValues.slice(1..(dataValues.size - 1))){
            Log.d("GROP", data.name)
            totals      = totals        + data.totals
            finished    = finished      + data.finished
            lowPriority = lowPriority   +   data.lowPriority
            mediumPriority  = mediumPriority    + data.mediumPriority
            highPriority    = highPriority      + data.highPriority
            pendings    = pendings  +   data.pendings
            canceled    = canceled  + data.canceled
            started     = started   + data.started
            revision    = revision  + data.revision
            outTime     = outTime   + data.outTime
            onTime      = onTime    + data.onTime
        }
        var groupStadistic = UserTaskDetailReport(
            "TEAM_ID_CREATED_BY_MOD_REPORT",
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
        return groupStadistic

    }


}
