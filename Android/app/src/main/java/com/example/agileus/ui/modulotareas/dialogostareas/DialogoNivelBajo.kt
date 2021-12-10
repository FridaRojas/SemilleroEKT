package com.example.agileus.ui.modulotareas.dialogostareas

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.agileus.R
import com.example.agileus.models.DataTask
import com.example.agileus.providers.DownloadProvider
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.modulotareas.detalletareas.DetalleNivelAltoViewModel
import com.example.agileus.ui.modulotareas.listenerstareas.dialogoConfirmarListener
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*

class DialogoNivelBajo(
    var dataTask: DataTask,
    var listener: dialogoConfirmarListener
) :
    DialogFragment() {

    private var txtDia: String = ""
    private var txtMes: String = ""
    private lateinit var estatusD: String
    private lateinit var fechaFinD: Date
    private lateinit var fechaInicioD: Date
    private lateinit var descripcionD: String
    private lateinit var prioridadD: String
    private lateinit var nombrePersonaD: String
    private lateinit var nombreTarea: String
    private lateinit var detalleNivelBajoViewModel: DetalleNivelAltoViewModel

    var mesI: String = ""
    var diaI: String = ""
    var mesF: String = ""
    var diaF: String = ""
    var fechaFi: String = ""
    var fechaIn: String = ""
    val sdf3 = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)

    val cal = Calendar.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            detalleNivelBajoViewModel =
                ViewModelProvider(this).get(DetalleNivelAltoViewModel::class.java)

            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val vista = inflater.inflate(R.layout.dialog_nivel_bajo, null)
            var txtNombreTareaD = vista.findViewById<TextView>(R.id.txtNombreTareaD)
            var txtNombrePersonaD = vista.findViewById<TextView>(R.id.txtNombrePersonaD)
            var txtPrioridadD = vista.findViewById<TextView>(R.id.txtPrioridadD)
            var txtDescripcionD = vista.findViewById<TextView>(R.id.txtDescripcionD)
            var txtFechaInicioD = vista.findViewById<TextView>(R.id.txtFechaInicioD)
            var txtFechaFinD = vista.findViewById<TextView>(R.id.txtFechaFinD)
            var txtObservacionesD = vista.findViewById<TextView>(R.id.txtObservacionesD)
            var txtEstatusD = vista.findViewById<TextView>(R.id.txtEstatusD)
            var btnEstado = vista.findViewById<Button>(R.id.btnCambiarEstadoD)
            var btnPdf = vista.findViewById<ImageView>(R.id.btnPdf)
            var progressBar = vista.findViewById<ProgressBar>(R.id.progressBar)
            var fechaI = sdf3.parse(dataTask.fechaIni.toString())
            var fechaF = sdf3.parse(dataTask.fechaFin.toString())

            sigEstatus(btnEstado)
            cal.time = fechaI
            convierteFechas(cal.time, 1)

            cal.time = fechaF
            convierteFechas(cal.time, 2)

            nombreTarea = dataTask.titulo
            nombrePersonaD = dataTask.nombreEmisor
            prioridadD = dataTask.prioridad
            estatusD = dataTask.estatus
            descripcionD = dataTask.descripcion
            fechaInicioD = dataTask.fechaIni
            fechaFinD = dataTask.fechaFin

            txtNombreTareaD.text = nombreTarea
            txtNombrePersonaD.text = nombrePersonaD.capitalize()
            txtPrioridadD.text = "Prioridad: ${prioridadD.capitalize()}"
            txtDescripcionD.text = "Descripción: $descripcionD"
            txtFechaInicioD.text = " Inicio: $fechaIn"
            txtFechaFinD.text = " Fin: $fechaFi"
            txtEstatusD.text = "Estatus: ${estatusD.capitalize()}"
            txtObservacionesD.text = "Observaciones: ${dataTask.observaciones}"

            activaElementosDisponibles(dataTask, txtObservacionesD, btnPdf, btnEstado, progressBar)

            this.dialog?.closeOptionsMenu()

            btnEstado.setOnClickListener {
                val newFragment = DialogoActualizarEstatus(dataTask, listener)
                newFragment.show(
                    (activity as HomeActivity).supportFragmentManager,
                    "missiles"
                )
                this.dialog?.dismiss()
            }
            builder.setView(vista)

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

    private fun activaElementosDisponibles(
        dataTask: DataTask,
        txtObservacionesD: TextView,
        btnPdf: ImageView,
        btnEstado: Button,
        progressBar: ProgressBar
    ) {

        if (dataTask.estatus.equals("revision")) {
            btnEstado.isEnabled = false
        }

        if (dataTask.observaciones != null) {
            txtObservacionesD.setText(dataTask.observaciones)
            txtObservacionesD.isVisible = true
        } else {
            txtObservacionesD.isVisible = false
            dataTask.observaciones = ""
        }

        if (!dataTask.archivo.isNullOrEmpty()) {
            Toast.makeText(context, dataTask.archivo, Toast.LENGTH_SHORT).show()
            btnPdf.isVisible = true
            btnPdf.setOnClickListener {
                // Log.d("Mensaje", dataTask.idTarea)
                progressBar.isVisible = true
                var mDownloadProvider = DownloadProvider()
                mDownloadProvider.dowloadFile(
                    (activity as HomeActivity).applicationContext,
                    dataTask.archivo, "archivo"
                )
                progressBar.isVisible = false

            }
        } else {
            btnPdf.isVisible = false
        }
    }

    private fun convierteFechas(time: Date, fecha: Int) {
        var anio = cal[Calendar.YEAR] + 1
        var mes = cal[Calendar.MONTH] + 1
        var dia = cal[Calendar.DATE] + 1
        if (mes < 10) {
            txtMes = "0$mes"
        } else {
            txtMes = mes.toString()
        }
        if (dia < 10) {
            txtDia = "0$dia"
        } else {
            txtDia = dia.toString()
        }

        if (fecha == 1) {
            fechaIn =
                cal[Calendar.YEAR].toString() + "-" + txtMes + "-" + txtDia
        } else if (fecha == 2) {
            fechaFi =
                cal[Calendar.YEAR].toString() + "-" + txtMes + "-" + txtDia
        }
    }

    private fun sigEstatus(btnEstado: Button) {
        if (dataTask.estatus.equals("pendiente")) {
            btnEstado.setText("Cambiar a Iniciada")
        } else if (dataTask.estatus.equals("iniciada")) {
            btnEstado.setText("Cambiar a Revision")
        } else if (dataTask.estatus.equals("revision")) {
            btnEstado.setText("En revision")
            btnEstado.isEnabled = false
        } else if (dataTask.estatus.equals("terminada")) {
            btnEstado.setText("Tarea Terminada")
            btnEstado.isEnabled = false
        } else if (dataTask.estatus.equals("cancelado")) {
            btnEstado.setText("Tarea Cancelada")
            btnEstado.isEnabled = false
        }
    }
}