package com.example.agileus.webservices.apis

import com.example.agileus.models.DatosTareas
import retrofit2.Call
import retrofit2.http.GET

interface ReportesApi {

    @GET("servicio.json?alt=media&token=e3076cf4-2c04-4609-ab51-8d3cbffdc6d8")
    fun getDatosReporteTareas(): Call<ArrayList<DatosTareas>>

}