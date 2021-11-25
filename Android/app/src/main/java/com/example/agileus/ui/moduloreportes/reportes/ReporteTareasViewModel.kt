package com.example.agileus.ui.moduloreportes.reportes

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agileus.adapters.ListaDatosAdapter
import com.example.agileus.models.Estadisticas
import com.example.agileus.ui.modulomensajeria.listacontactos.HomeViewModel
import com.example.agileus.webservices.dao.ReporteTareasDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReporteTareasViewModel : ViewModel() {

    var adaptador = MutableLiveData<ListaDatosAdapter>()
    var lista : ReporteTareasDao

    init{
        lista= ReporteTareasDao()
    }

    private lateinit var listaConsumida:ArrayList<Estadisticas>

    fun devuelvelistaReporte(){
        try {
            viewModelScope.launch {
                listaConsumida =  withContext(Dispatchers.IO) {
                    lista.recuperardatosTareas()
                }
                if(listaConsumida.isNotEmpty()){

                    adaptador.value = ListaDatosAdapter(listaConsumida)
                }
            }

        }catch (ex:Exception){
            Log.e(HomeViewModel::class.simpleName.toString(), ex.message.toString())
        }
    }
}