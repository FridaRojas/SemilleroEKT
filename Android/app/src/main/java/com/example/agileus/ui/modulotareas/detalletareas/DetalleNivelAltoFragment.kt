package com.example.agileus.ui.modulotareas.detalletareas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.agileus.databinding.FragmentDetalleNivelAltoBinding


private var _binding: FragmentDetalleNivelAltoBinding? = null
private val binding get() = _binding!!

class DetalleNivelAltoFragment : Fragment() {
    private lateinit var detalleNivelAltoViewModel: DetalleNivelAltoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        detalleNivelAltoViewModel =
            ViewModelProvider(this).get(DetalleNivelAltoViewModel::class.java)

        _binding = FragmentDetalleNivelAltoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: DetalleNivelAltoFragmentArgs by navArgs()

        var nombreTarea = args.tareas.titulo
        var nombrePersona = args.tareas.nombreEmisor
        var prioridad = args.tareas.prioridad
        // var estatus = args.tareas.estatus
        var descripcion = args.tareas.descripcion
        var fechaInicio = args.tareas.fechaIni
        var fechaFin = args.tareas.fechaFin
        // var observaciones = args.tareas.observaciones

        with(binding) {
            txtNombreTareaD.text = nombreTarea
            txtNombrePersonaD.text = nombrePersona
            txtPrioridadD.text = prioridad
            txtDescripcionD.text = descripcion
            //txtFechaInicioD.text = fechaInicio.toString()
            //txtFechaFinD.text = fechaFin.toString()
        }

        binding.btnCancelarTareaF.setOnClickListener {
            cancelarTarea(args)
        }


    }

    private fun cancelarTarea(args: DetalleNivelAltoFragmentArgs) {
//        args.estatus = "Cancelada"

        detalleNivelAltoViewModel.cancelarTarea(args)
    }

}