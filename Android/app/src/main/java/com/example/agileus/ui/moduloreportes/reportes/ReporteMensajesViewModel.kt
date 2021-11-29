package com.example.agileus.ui.moduloreportes.reportes

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.ListaDatosAdapter
import com.example.agileus.models.Contacts
import com.example.agileus.models.EmployeeListByBossID
import com.example.agileus.models.Estadisticas
import com.example.agileus.utils.Constantes
import com.example.agileus.providers.ReportesListener
import com.example.agileus.webservices.dao.ReporteMensajesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReporteMensajesViewModel: ViewModel() {

    var adaptador = MutableLiveData<ListaDatosAdapter>()
    var enviados = MutableLiveData<String>()
    var recibidos = MutableLiveData<String>()
    var totales = MutableLiveData<String>()
    var leidos = MutableLiveData<String>()
    var lista : ReporteMensajesDao
    var cargaDatosExitosa = MutableLiveData<Boolean>()

    private lateinit var listaConsumida:ArrayList<Estadisticas>
    lateinit var listaHijosConsumida:ArrayList<Contacts>

    init {

        lista = ReporteMensajesDao()
        enviados.value = "0"
        recibidos.value =  "0"
        totales.value =  "0"
        leidos.value =  "0"
        cargaDatosExitosa.value = false

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun devuelvelistaReporte(listener: ReportesListener){

            viewModelScope.launch {
                listaConsumida =  withContext(Dispatchers.IO) {
                    lista.recuperardatosMensajes()
                }
                if(listaConsumida.isNotEmpty()){
                    adaptador.value = ListaDatosAdapter(listaConsumida,listener)
                    enviados.value = lista.obtenerMensajesEnviados()
                    recibidos.value = lista.obtenerMensajesRecibidos()
                    totales.value = lista.obtenerMensajesTotales()
                    leidos.value = lista.obtenerMensajesLeidos()

                    if(enviados.value!!.isNotEmpty() && recibidos.value!!.isNotEmpty()
                        && totales.value!!.isNotEmpty() && leidos.value!!.isNotEmpty()){
                        cargaDatosExitosa.value = true
                    }
                }
            }
    }

    fun devuelveListaEmpleados(idUser:String){
        try {
            viewModelScope.launch {
                listaHijosConsumida =  withContext(Dispatchers.IO) {
                    lista.obtenerListaSubContactos(idUser)
                }
                listaHijosConsumida.forEach {
                    Log.e("Hijos", it.nombre)
                }

                Log.i("sizeList", "${listaHijosConsumida.size}")
            }
        }catch (ex:Exception){
            Log.e(ReporteMensajesViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }
}