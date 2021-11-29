package com.example.agileus.ui.moduloreportes.reportes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.ListaDatosAdapter
import com.example.agileus.models.Estadisticas
import com.example.agileus.providers.ReportesListener
import com.example.agileus.webservices.dao.ReporteTareasDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReporteTareasViewModel() : ViewModel() {

    var adaptador = MutableLiveData<ListaDatosAdapter>()
    var terminadas = MutableLiveData<String>()
    var pendientes = MutableLiveData<String>()
    var totales = MutableLiveData<String>()
    var leidas = MutableLiveData<String>()
    var lista : ReporteTareasDao// este es el dao del que se va a traer
    var cargaDatosExitosa = MutableLiveData<Boolean>()


    init {

        lista = ReporteTareasDao()
        terminadas.value = "0"
        pendientes.value =  "0"
        totales.value =  "0"
        leidas.value =  "0"
        cargaDatosExitosa.value = false

    }

    private lateinit var listaConsumida:ArrayList<Estadisticas>

    @RequiresApi(Build.VERSION_CODES.O)
    fun devuelvelistaReporte(listener: ReportesListener){

        viewModelScope.launch {
            listaConsumida =  withContext(Dispatchers.IO) {
                lista.recuperardatosTareas()
            }
            if(listaConsumida.isNotEmpty()){
                adaptador.value = ListaDatosAdapter(listaConsumida,listener)
                terminadas.value = lista.obtenerTareasTerminadas()
                pendientes.value = lista.obtenerTareasPendientes()
                totales.value = lista.obtenerTareasTotales()
                leidas.value = lista.obtenerTareasLeidas()

                if(terminadas.value!!.isNotEmpty() && pendientes.value!!.isNotEmpty()
                    && totales.value!!.isNotEmpty() && leidas.value!!.isNotEmpty()){
                    cargaDatosExitosa.value = true
                }
            }
        }

    }
}