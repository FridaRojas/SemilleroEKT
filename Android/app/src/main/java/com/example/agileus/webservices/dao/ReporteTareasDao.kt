package com.example.agileus.webservices.dao

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.agileus.R
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.DatosTareas
import com.example.agileus.models.Estadisticas
import retrofit2.Response
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class ReporteTareasDao {


    private var contador_tareas_terminadas:Int = 0
    private var contador_tareas_pendientes:Int = 0
    private var contador_tareas_leidas:Int=0
    private var contador_tareas_totales:Int=0

    private var diferencia_horas: Long = 0
    private var fecha_actual: ZonedDateTime? = null
    private var fecha_anterior: ZonedDateTime? = null
    private var suma_tiempos:Int=0
    private var temporal:Int=0
    private lateinit var promedio_tiempo_respuesta:String

    @RequiresApi(Build.VERSION_CODES.O)
    fun recuperardatosTareas(): ArrayList<Estadisticas> {

        val callRespuesta = InitialApplication.webServiceGlobalReportes.getDatosReporteTareas()
        val ResponseTareas: Response<ArrayList<DatosTareas>> = callRespuesta.execute()

        var listaRecycler= ArrayList<Estadisticas>()
        val lista: ArrayList<DatosTareas>

        if (ResponseTareas.isSuccessful) {
            lista = ResponseTareas.body()!!

            val id_receptor = lista[0].id_receptor //Aquí se coloca el id del usuario a revisar

            var contador_t_terminadas= 0
            var contador_t_pendientes = 0
            var contador_t_leidas=0
            var contador_t_totales=0
            var tiempo_p_respuesta=""
            temporal=0
            suma_tiempos=0
            promedio_tiempo_respuesta=""


            fecha_anterior = ZonedDateTime.parse(lista[0].fecha_ini) // primera fecha para comparar

            lista.forEach {

                if(id_receptor==it.id_receptor) {

                    contador_t_totales = contador_t_totales + 1

                    if (it.leido) {
                        contador_t_leidas = contador_t_leidas + 1
                    }
                        if (it.status.equals("conpletada")) {
                            contador_t_terminadas = contador_t_terminadas + 1
                        } else {
                            contador_t_pendientes = contador_t_pendientes + 1
                        }
                }

                fecha_actual = ZonedDateTime.parse(it.fecha_ini)
                diferencia_horas = ChronoUnit.HOURS.between(fecha_anterior, fecha_actual)

                Log.d("dias","diferencia días: ${diferencia_horas}")

                suma_tiempos = suma_tiempos + diferencia_horas.toInt()

                Log.d("dias","suma tiempos: ${suma_tiempos}")

                fecha_anterior = fecha_actual

                temporal=temporal+1

            }

            if(temporal==0)
                tiempo_p_respuesta = "Sin tiempo de respuesta."
            else {

                var t_respuesta= ((suma_tiempos) / (temporal - 1)) / 24

                if(t_respuesta==1)
                tiempo_p_respuesta = "${t_respuesta} día."
                else
                tiempo_p_respuesta = "${t_respuesta} días."

            }
            Log.d("dias","suma tiempos: ${suma_tiempos}")

            contador_tareas_terminadas=contador_t_terminadas
            contador_tareas_pendientes=contador_t_pendientes
            contador_tareas_totales=contador_t_totales
            contador_tareas_leidas=contador_t_leidas
            promedio_tiempo_respuesta=tiempo_p_respuesta


            listaRecycler.add(Estadisticas("Terminadas",contador_tareas_terminadas.toString(),"Pendientes",contador_tareas_pendientes.toString(), R.drawable.ic_pie_chart))
            listaRecycler.add(Estadisticas("Promedio de respuesta del Broadcast:","","",promedio_tiempo_respuesta, R.drawable.ic_bar_chart))

            Log.d("dias","promedio respuesta: ${promedio_tiempo_respuesta}")
        }

        return listaRecycler
    }


    fun obtenerTareasTerminadas():String{

        return contador_tareas_terminadas.toString()
    }

    fun obtenerTareasPendientes():String{

        return contador_tareas_pendientes.toString()
    }

    fun obtenerTareasTotales():String{

        return contador_tareas_totales.toString()
    }

    fun obtenerTareasLeidas():String{

        return contador_tareas_leidas.toString()
    }


}
