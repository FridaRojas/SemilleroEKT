package com.example.agileus.webservices.dao

import com.example.agileus.R
import com.example.agileus.config.InitialApplication
import com.example.agileus.models.DatosTareas
import com.example.agileus.models.Estadisticas
import retrofit2.Response

class ReporteTareasDao {

    fun recuperardatosTareas(vista: Int): ArrayList<Estadisticas> {

        val callRespuesta = InitialApplication.webServiceGlobalReportes.getDatosReporteTareas()
        val ResponseTareas: Response<ArrayList<DatosTareas>> = callRespuesta.execute()

        //var lista = ArrayList<DatosTareas>()
        var listaRecycler= ArrayList<Estadisticas>()

        if (ResponseTareas.isSuccessful) {
            val lista = ResponseTareas.body()!!

            if(vista==1){
                listaRecycler.add(Estadisticas("Enviados",lista.size.toString(),"Recibidos",lista.size.toString(), R.drawable.iconomensajes))
            }
            else
                if(vista==2){
                    listaRecycler.add(Estadisticas("Terminadas",lista.size.toString(),"Pendientes",lista.size.toString(), R.drawable.iconomensajes))

                }

            listaRecycler.add(Estadisticas("Tiempo de respuesta promedio","","",lista.size.toString(), R.drawable.ic_reportes))

        }

        return listaRecycler

        }
        //return lista

    }
