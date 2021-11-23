package com.example.agileus.ui.moduloreportes.reportes

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.ListaDatosAdapter
import com.example.agileus.models.DatosTareas
import com.example.agileus.models.Estadisticas
import com.example.agileus.ui.modulomensajeria.listacontactos.HomeViewModel
import com.example.agileus.webservices.dao.ReporteTareasDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReporteTareasViewModel : ViewModel() {

    var adaptador = MutableLiveData<ListaDatosAdapter>()
    lateinit var lista : ReporteTareasDao

    init{
        lista= ReporteTareasDao()
    }

    private lateinit var listaConsumida:ArrayList<Estadisticas>

    fun devuelvelistaReporte(vista:Int){
        try {
            viewModelScope.launch {
                listaConsumida =  withContext(Dispatchers.IO) {
                    lista.recuperardatosTareas(vista)
                }
                if(listaConsumida.isNotEmpty()){

                    adaptador.value = ListaDatosAdapter(listaConsumida)
                }
            }

        }catch (ex:Exception){
            Log.e(HomeViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }


    private val _text = MutableLiveData<String>().apply {
        value = "This is Tareas reportes Fragment"
    }
    val text: LiveData<String> = _text
}