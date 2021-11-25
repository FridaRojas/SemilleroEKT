package com.example.agileus.webservices.dao

import com.example.agileus.R
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.DatosTareas
import com.example.agileus.models.Estadisticas
import retrofit2.Response

class ReporteTareasDao {

    fun recuperardatosTareas(): ArrayList<Estadisticas> {

        val callRespuesta = InitialApplication.webServiceGlobalReportes.getDatosReporteTareas()
        val ResponseTareas: Response<ArrayList<DatosTareas>> = callRespuesta.execute()

        var listaRecycler= ArrayList<Estadisticas>()

        if (ResponseTareas.isSuccessful) {
            val lista = ResponseTareas.body()!!
            listaRecycler.add(Estadisticas("Terminadas",lista.size.toString(),"Pendientes",lista.size.toString(), R.drawable.ic_pie_chart))
            listaRecycler.add(Estadisticas("Tiempo de respuesta promedio","","",lista.size.toString(), R.drawable.ic_bar_chart))

        }

        return listaRecycler
    }
}
