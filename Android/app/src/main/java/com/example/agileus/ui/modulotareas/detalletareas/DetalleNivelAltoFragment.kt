package com.example.agileus.ui.modulotareas.detalletareas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.agileus.databinding.FragmentDetalleNivelAltoBinding


private var _binding: FragmentDetalleNivelAltoBinding? = null
private val binding get() = _binding!!

class DetalleNivelAltoFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetalleNivelAltoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: DetalleNivelAltoFragmentArgs by navArgs()

        var nombreTarea = args.tareas.titulo
        var nombrePersona = args.tareas.nombreReceptor
        var prioridad = args.tareas.prioridad
        //    var estatus = args.tarea.estatus
        var descripcion = args.tareas.descripcion
        var fechaInicio = args.tareas.fechaIni
        var fechaFin = args.tareas.fechaFin
        //var observaciones = args.tarea.observaciones

        with(binding) {
            txtNombreTareaD.text = nombreTarea
            txtNombrePersonaD.text = nombrePersona
            txtPrioridadD.text = prioridad
            txtDescripcionD.text = descripcion
            //txtFechaInicioD.text = fechaInicio.toString()
            //txtFechaFinD.text = fechaFin.toString()
        }


    }

}