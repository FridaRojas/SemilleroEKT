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
import com.example.agileus.models.TaskUpdate
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.modulotareas.dialogostareas.DialogoAceptar
import com.example.agileus.ui.modulotareas.dialogostareas.EdtFecha
import com.example.agileus.ui.modulotareas.listenerstareas.DialogoFechaListener
import java.text.SimpleDateFormat
import java.util.*


class DetalleNivelAltoFragment : Fragment(), DialogoFechaListener {
    private var anioFin: Int = 0
    private var mesInicio: Int = 0
    private var anioInicio: Int = 0
    private var diaInicio = 0
    private var diaFin = 0
    private var mesFin = 0
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

            btnObservacionF.setOnClickListener {
                txtObservacionesD.isVisible = true
                txtObservacionesD.isEnabled = true
            }

            btnGuardarTareaF.setOnClickListener {
                var obs = binding.txtObservacionesD.text.toString()
                args.tareas.observaciones = obs

                var titulo: String
                var descripcion: String
                var fecha_ini: String
                var fecha_fin: String
                var prioridad: String
                var estatus: String
                var observaciones: String


                titulo = txtNombreTareaD.text.toString()
                descripcion = txtDescripcionD.text.toString()
                fecha_ini = txtFechaFinD.text.toString()
                fecha_fin = txtFechaInicioD.text.toString()
                prioridad = txtPrioridadD.text.toString()
                estatus = txtEstatusD.text.toString()
                observaciones = obs

                var update = TaskUpdate(
                    titulo,
                    descripcion,
                    fecha_ini,
                    fecha_fin,
                    prioridad,
                    estatus,
                    observaciones
                )

                detalleNivelAltoViewModel.editarTarea(update, args.tareas.idTarea)
            }

        }

        binding.txtFechaInicioD.setOnClickListener {
            val newFragment = EdtFecha(this, 1)
            newFragment.show(parentFragmentManager, "Edt fecha")
        }

        binding.txtFechaFinD.setOnClickListener {
            val newFragment = EdtFecha(this, 2)
            newFragment.show(parentFragmentManager, "Edt fecha")
        }

    }

    private fun setInfo(args: DetalleNivelAltoFragmentArgs) {
        val sdf3 = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)

        var fechaI = sdf3.parse(args.tareas.fechaIni.toString())
        var fechaF = sdf3.parse(args.tareas.fechaFin.toString())

        val cal = Calendar.getInstance()
        cal.time = fechaI
        var fechaIn =
            cal[Calendar.YEAR].toString() + "-" + (cal[Calendar.MONTH] + 1) + "-" + cal[Calendar.DATE]
        fechaIn.trim()
        Log.d("Mensaje", "fecha nueva $fechaIn")

        cal.time = fechaF
        var fechaFi =
            cal[Calendar.YEAR].toString() + "-" + (cal[Calendar.MONTH] + 1) + "-" + cal[Calendar.DATE]
        fechaFi.trim()
        Log.d("Mensaje", "fecha nueva $fechaIn")
        nombreTarea = args.tareas.titulo
        nombrePersona = args.tareas.nombreEmisor
        prioridad = args.tareas.prioridad
        estatus = args.tareas.estatus
        descripcion = args.tareas.descripcion
//        fechaInicio = fechaIn
//        fechaFin = fechaF
        Log.d("Mensaje", "fecha inicio $fechaI")
        if (args.tareas.observaciones != null) {
            observaciones = args.tareas.observaciones
            binding.txtObservacionesD.setText(observaciones)
            binding.txtObservacionesD.isVisible = true
        } else {
            binding.txtObservacionesD.isVisible = false
            observaciones = ""
        }

        with(binding) {
            txtNombreTareaD.setText(nombreTarea)
            txtNombrePersonaD.text = nombrePersona
            txtPrioridadD.text = prioridad
            txtDescripcionD.setText(descripcion)
            txtEstatusD.setText(estatus)
            txtFechaInicioD.setText(fechaIn)
            txtFechaFinD.setText(fechaFi)
            txtObservacionesD.setText(observaciones)
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
            btnGuardarTareaF.isVisible = false
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
            btnGuardarTareaF.isVisible = true
        }
    }

    private fun editarTarea(args: DetalleNivelAltoFragmentArgs) {
        //args.tareas.prioridad = binding.txtPrioridadD.text.toString()
        var des = binding.txtDescripcionD.text.toString()
        args.tareas.descripcion = des
        Toast.makeText(context, "$des", Toast.LENGTH_SHORT).show()
        // args.tareas.fechaIni = binding.txtFechaInicioD.text.toString()
        // args.tareas.fechaFin = binding.txtFechaFinD.text.toString()
        // args.tareas.observaciones = binding.txtObservacionesD.text.toString()
    }

    private fun cancelarTarea(args: DetalleNivelAltoFragmentArgs) {
        val dialogoAceptar = DialogoAceptar(args)
        dialogoAceptar.show(
            (activity as HomeActivity).supportFragmentManager,
            getString(R.string.dialogoAceptar)
        )
    }

    override fun onDateInicioSelected(anio: Int, mes: Int, dia: Int) {


    }

    override fun onDateFinSelected(anio: Int, mes: Int, dia: Int) {

    }

}