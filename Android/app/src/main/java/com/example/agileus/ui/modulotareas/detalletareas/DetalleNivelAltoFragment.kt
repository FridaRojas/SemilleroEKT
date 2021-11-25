package com.example.agileus.ui.modulotareas.detalletareas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
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
            txtNombreTareaD.setText(nombreTarea)
            txtNombrePersonaD.text = nombrePersona
            txtPrioridadD.text = prioridad
            txtDescripcionD.setText(descripcion)
            //txtFechaInicioD.text = fechaInicio.toString()
            //txtFechaFinD.text = fechaFin.toString()
        }

        binding.btnCancelarTareaF.setOnClickListener {
            cancelarTarea(args)
        }

        binding.btnEditarTareaF.setOnClickListener {
            binding.txtDescripcionD.isEnabled = true
            binding.txtDescripcionD.isEnabled = true
            binding.txtFechaInicioD.isEnabled = true
            binding.txtFechaFinD.isEnabled = true
            binding.txtObservacionesD.isEnabled = true
            binding.btnAdjuntarArchivoF.setText("Guardar Tarea")
            binding.btnEditarTareaF.isVisible = false
            binding.btnCancelarTareaF.isVisible = false
            binding.btnObservacionF.isVisible = false
            binding.btnCancelarEdicion.isVisible = true
        }


        binding.btnCancelarEdicion.setOnClickListener {
            binding.txtDescripcionD.isEnabled = false
            binding.txtDescripcionD.isEnabled = false
            binding.txtFechaInicioD.isEnabled = false
            binding.txtFechaFinD.isEnabled = false
            binding.txtObservacionesD.isEnabled = false
            binding.btnAdjuntarArchivoF.setText("Adjuntar Archivo")
            binding.btnEditarTareaF.isVisible = true
            binding.btnCancelarTareaF.isVisible = true
            binding.btnObservacionF.isVisible = true
            binding.btnCancelarEdicion.isVisible = false
        }

        binding.btnAdjuntarArchivoF.setOnClickListener {
            if (binding.btnAdjuntarArchivoF.text.equals("Adjuntar Archivo")) {

            } else if (binding.btnAdjuntarArchivoF.text.equals("Guardar Tarea")) {
                editarTarea(args)
            }

        }
    }

    private fun editarTarea(args: DetalleNivelAltoFragmentArgs) {
        detalleNivelAltoViewModel.editarTarea(args)
    }

    private fun cancelarTarea(args: DetalleNivelAltoFragmentArgs) {
        detalleNivelAltoViewModel.cancelarTarea(args)
    }

}