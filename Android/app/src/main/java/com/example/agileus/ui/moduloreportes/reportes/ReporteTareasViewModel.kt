package com.example.agileus.ui.moduloreportes.reportes

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.ListaDatosAdapter
import com.example.agileus.models.Estadisticas
import com.example.agileus.models.UserTaskDetailReport
import com.example.agileus.providers.ReportesListener
import com.example.agileus.webservices.dao.ReporteTareasDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReporteTareasViewModel: ViewModel() {

    var adaptador = MutableLiveData<ListaDatosAdapter>()
    var terminadas = MutableLiveData<String>()
    var pendientes = MutableLiveData<String>()
    var iniciadas = MutableLiveData<String>()
    var revision = MutableLiveData<String>()
    var aTiempo = MutableLiveData<String>()
    var fueraTiempo = MutableLiveData<String>()
    var totales = MutableLiveData<String>()
    var leidas = MutableLiveData<String>()
    var lista : ReporteTareasDao// este es el dao del que se va a traer
    var cargaDatosExitosa = MutableLiveData<Boolean>()
    var cargaOperacionesEstadisticasTareas = MutableLiveData<Boolean>()


    init {
        lista = ReporteTareasDao()
        terminadas.value = "0"
        pendientes.value =  "0"
        iniciadas.value = "0"
        revision.value =  "0"
        aTiempo.value = "0"
        fueraTiempo.value =  "0"
        totales.value =  "0"
        leidas.value =  "0"
        cargaDatosExitosa.value = false
        cargaOperacionesEstadisticasTareas.value = false

    }

    private lateinit var listaConsumida:ArrayList<Estadisticas>
    lateinit var listaHijosConsumida:ArrayList<UserTaskDetailReport>

    @RequiresApi(Build.VERSION_CODES.O)
    fun devuelvelistaReporte( listener: ReportesListener, id:String){
        viewModelScope.launch {
            listaConsumida =  withContext(Dispatchers.IO) {
                lista.recuperardatosTareas(id)
            }
            if(listaConsumida.isNotEmpty()){
                adaptador.value = ListaDatosAdapter(listaConsumida,listener)
                terminadas.value = lista.obtenerTareasTerminadas()
                pendientes.value = lista.obtenerTareasPendientes()
                iniciadas.value = lista.obtenerTareasIniciadas()
                revision.value = lista.obtenerTareasEnRevision()
                aTiempo.value = lista.obtenerTareasATiempo()
                fueraTiempo.value = lista.obtenerTareasFueraTiempo()
                totales.value = lista.obtenerTareasTotales()
                leidas.value = lista.obtenerTareasLeidas()

                if(terminadas.value!!.isNotEmpty() && pendientes.value!!.isNotEmpty()
                    && totales.value!!.isNotEmpty() && leidas.value!!.isNotEmpty()){
                    cargaDatosExitosa.value = true
                }
            }
        }
    }
    var listaEmpleadosAux = MutableLiveData<ArrayList<UserTaskDetailReport>>()

    @RequiresApi(Build.VERSION_CODES.O)
    fun devuelveListaEmpleados(idUser:String){
        try {
            viewModelScope.launch {
                listaHijosConsumida =  withContext(Dispatchers.IO) {
                    lista.obtenerListaSubContactos(idUser)
                }
                if (listaHijosConsumida.isNotEmpty()){
                    listaEmpleadosAux.value = listaHijosConsumida
                    cargaOperacionesEstadisticasTareas.value = true
                }
            }
        }catch (ex:Exception){
            Log.e(ReporteMensajesViewModel::class.simpleName.toString(), ex.message.toString())
        }

    }
}