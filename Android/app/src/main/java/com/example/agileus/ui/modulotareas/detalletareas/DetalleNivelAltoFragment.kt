package com.example.agileus.ui.modulotareas.detalletareas

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.agileus.R
import com.example.agileus.databinding.FragmentDetalleNivelAltoBinding
import com.example.agileus.models.DataTask
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.modulotareas.dialogostareas.DialogoAceptar
import com.example.agileus.ui.modulotareas.dialogostareas.EdtFecha
import com.example.agileus.ui.modulotareas.listenerstareas.DialogosFormularioCrearTareasListener
import java.util.*


class DetalleNivelAltoFragment : Fragment(), DialogosFormularioCrearTareasListener {
    private var _binding: FragmentDetalleNivelAltoBinding? = null
    private val binding get() = _binding!!
    private lateinit var observaciones: String
    private lateinit var fechaFin: Date
    private lateinit var fechaInicio: Date
    private lateinit var descripcion: String
    private lateinit var estatus: String
    private lateinit var prioridad: String
    private lateinit var nombrePersona: String
    private lateinit var nombreTarea: String
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
        setInfo(args)
        with(binding) {

            btnCancelarTareaF.setOnClickListener {
                cancelarTarea(args)
            }

            btnEditarTareaF.setOnClickListener {
                activarCampos()
            }

            btnCancelarEdicion.setOnClickListener {
                desactivarCampos()
            }

            btnAdjuntarArchivoF.setOnClickListener {
                if (binding.btnAdjuntarArchivoF.text.equals(getString(R.string.AdjuntarArchivo))) {
                } else if (binding.btnAdjuntarArchivoF.text.equals(getString(R.string.GuardarTarea))) {
                    editarTarea(args)
                }
            }

        }
        binding.txtFechaInicioD.setOnClickListener {
            val newFragment = EdtFecha(this, 1)
            newFragment.show(parentFragmentManager, "Edt fecha")
        }

    }

    private fun setInfo(args: DetalleNivelAltoFragmentArgs) {
        nombreTarea = args.tareas.titulo
        nombrePersona = args.tareas.nombreEmisor
        prioridad = args.tareas.prioridad
        //estatus = args.tareas.estatus
        descripcion = args.tareas.descripcion
        //fechaInicio = args.tareas.fechaIni
        //fechaFin = args.tareas.fechaFin
        //observaciones = args.tareas.observaciones

        with(binding) {
            txtNombreTareaD.setText(nombreTarea)
            txtNombrePersonaD.text = nombrePersona
            txtPrioridadD.text = prioridad
            txtDescripcionD.setText(descripcion)
            //txtFechaInicioD.text = fechaInicio.toString()
            //txtFechaFinD.text = fechaFin.toString()
        }
    }

    private fun desactivarCampos() {
        with(binding) {
            txtDescripcionD.isEnabled = false
            txtDescripcionD.isEnabled = false
            txtFechaInicioD.isEnabled = false
            txtFechaFinD.isEnabled = false
            txtObservacionesD.isEnabled = false
            btnAdjuntarArchivoF.setText(getString(R.string.AdjuntarArchivo))
            btnEditarTareaF.isVisible = true
            btnCancelarTareaF.isVisible = true
            btnObservacionF.isVisible = true
            btnCancelarEdicion.isVisible = false
        }
    }

    private fun activarCampos() {
        with(binding) {
            txtDescripcionD.isEnabled = true
            txtDescripcionD.isEnabled = true
            txtFechaInicioD.isEnabled = true
            txtFechaFinD.isEnabled = true
            txtObservacionesD.isEnabled = true
            btnAdjuntarArchivoF.setText(getString(R.string.GuardarTarea))
            btnEditarTareaF.isVisible = false
            btnCancelarTareaF.isVisible = false
            btnObservacionF.isVisible = false
            btnCancelarEdicion.isVisible = true
        }
    }

    private fun editarTarea(args: DetalleNivelAltoFragmentArgs) {
        //args.tareas.prioridad = binding.txtPrioridadD.text.toString()
        args.tareas.descripcion = binding.txtDescripcionD.text.toString()
        Toast.makeText(context, "${args.tareas.descripcion}", Toast.LENGTH_SHORT).show()
        // args.tareas.fechaIni = binding.txtFechaInicioD.text.toString()
        // args.tareas.fechaFin = binding.txtFechaFinD.text.toString()
       // args.tareas.observaciones = binding.txtObservacionesD.text.toString()
        Log.d("Mensaje", args.toString())
        detalleNivelAltoViewModel.editarTarea(args)
    }

    private fun cancelarTarea(args: DetalleNivelAltoFragmentArgs) {
        val dialogoAceptar = DialogoAceptar()
        dialogoAceptar.show(
            (activity as HomeActivity).supportFragmentManager,
            getString(R.string.dialogoAceptar)
        )
        var resp = dialogoAceptar.resp
        if (resp == true) {
            //todo si no jala anadimos un observable para la respuesta del dialogo
            detalleNivelAltoViewModel.cancelarTarea(args)
        }
    }

    override fun onDateInicioSelected(anio: Int, mes: Int, dia: Int) {
        TODO("Not yet implemented")
    }

    override fun onDateFinSelected(anio: Int, mes: Int, dia: Int) {
        TODO("Not yet implemented")
    }

}