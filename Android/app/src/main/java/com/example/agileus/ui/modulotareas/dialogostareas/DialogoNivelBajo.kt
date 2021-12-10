package com.example.agileus.ui.modulotareas.dialogostareas

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.agileus.R
import com.example.agileus.models.DataTask
import com.example.agileus.providers.DownloadProvider
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.modulotareas.detalletareas.DetalleNivelAltoViewModel
import com.example.agileus.ui.modulotareas.listenerstareas.TaskListListener
import java.lang.IllegalStateException
import java.text.SimpleDateFormat
import java.util.*

class DialogoNivelBajo(private var listener: TaskListListener, var dataTask: DataTask) :
    DialogFragment() {

    private lateinit var estatusD: String
    private lateinit var fechaFinD: Date
    private lateinit var fechaInicioD: Date
    private lateinit var descripcionD: String
    private lateinit var prioridadD: String
    private lateinit var nombrePersonaD: String
    private lateinit var nombreTarea: String
    private lateinit var detalleNivelBajoViewModel: DetalleNivelAltoViewModel

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

            Log.d("Mensaje", dataTask.toString())

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

            var mesI: String = ""
            var diaI: String = ""
            var mesF: String = ""
            var diaF: String = ""
            var fechaFi: String = ""
            var fechaIn: String = ""
            val sdf3 = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)

            val cal = Calendar.getInstance()
            var fechaI = sdf3.parse(dataTask.fechaIni.toString())
            var fechaF = sdf3.parse(dataTask.fechaFin.toString())

            cal.time = fechaI

            cal[Calendar.MONTH] + 1
//        cal[Calendar.DATE] + 1
            if (cal[Calendar.MONTH] < 10) {
                mesI = "0${cal[Calendar.MONTH]}"
            } else {
                mesI = cal[Calendar.MONTH].toString()
            }

            if (cal[Calendar.DATE] < 10) {
                diaI = "0${cal[Calendar.DATE]}"
            } else {
                diaI = cal[Calendar.DATE].toString()
            }

            fechaIn =
                cal[Calendar.YEAR].toString() + "-" + mesI + "-" + diaI

///////////////////////////////////////////////////////////////777
            cal.time = fechaF

            cal[Calendar.MONTH] + 1
//        cal[Calendar.DATE] + 1
            if (cal[Calendar.MONTH] < 10) {
                mesF = "0${cal[Calendar.MONTH]}"
            } else {
                mesF = cal[Calendar.MONTH].toString()
            }

            if (cal[Calendar.DATE] < 10) {
                diaF = "0${cal[Calendar.DATE]}"
            } else {
                diaF = cal[Calendar.DATE].toString()
            }

            fechaFi =
                cal[Calendar.YEAR].toString() + "-" + mesF + "-" + diaF
            Log.d("Mensaje", "fecha nueva $fechaFi")

            nombreTarea = dataTask.titulo
            nombrePersonaD = dataTask.nombreEmisor
            prioridadD = dataTask.prioridad
            estatusD = dataTask.estatus
            descripcionD = dataTask.descripcion
            fechaInicioD = dataTask.fechaIni
            fechaFinD = dataTask.fechaFin
            //observacionesD = dataTask.observaciones

            txtNombreTareaD.text = nombreTarea
            txtNombrePersonaD.text = nombrePersonaD.capitalize()
            txtPrioridadD.text = "Prioridad: ${prioridadD.capitalize()}"
            txtDescripcionD.text = "Descripción: $descripcionD"
            txtFechaInicioD.text = " Inicio: $fechaIn"
            txtFechaFinD.text = " Fin: $fechaFi"
            txtEstatusD.text = "Estatus: ${estatusD.capitalize()}"
            txtObservacionesD.text = "Observaciones: ${dataTask.observaciones}"

            if (dataTask.observaciones != null) {
                //observacionesD = dataTask.observaciones
                txtObservacionesD.setText(dataTask.observaciones)
                txtObservacionesD.isVisible = true
            } else {
                txtObservacionesD.isVisible = false
                dataTask.observaciones = ""
            }

            this.dialog?.closeOptionsMenu()
            //Toast.makeText(context, dataTask.archivo, Toast.LENGTH_SHORT).show()
            if (!dataTask.archivo.isNullOrEmpty()) {
                btnPdf.isVisible = true
                btnPdf.setOnClickListener {
                    Log.d("Mensaje", dataTask.idTarea)
                    var mDownloadProvider = DownloadProvider()
                    mDownloadProvider.dowloadFile(
                        (activity as HomeActivity).applicationContext,
                        dataTask.archivo, "archivo"
                    )
                }
            } else {
                btnPdf.isVisible = false
            }

            btnEstado.setOnClickListener {
           /*     if (dataTask.estatus.equals("pendiente")) {
                    dataTask.estatus = "iniciada"
                } else if (dataTask.estatus.equals("iniciada")) {
                    dataTask.estatus = "revision"
                }*/
                if (dataTask.estatus.equals("revision")) {
                    btnEstado.isEnabled = false
                }

                val newFragment = DialogoActualizarEstatus(dataTask)
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
}