package com.example.agileus.webservices.apis

import com.example.agileus.models.DatosMensajes
import com.example.agileus.models.DatosTareas
import retrofit2.Call
import retrofit2.http.GET

interface ReportesApi {

    //@GET("Task.json?alt=media&token=bb6a2086-2e39-411a-8385-2294dabcc2d5")

    @GET("Task.json?alt=media&token=36678616-ea9f-4293-a347-3c6c23fd9e5a")
    fun getDatosReporteTareas(): Call<ArrayList<DatosTareas>>

    //@GET("Messages.json?alt=media&token=03022225-583c-4114-a056-ce4964b1a928")

    @GET("Messages.json?alt=media&token=39312937-7454-48f1-aa82-5ba279dee438")
    fun getDatosReporteMensajes(): Call<ArrayList<DatosMensajes>>

}