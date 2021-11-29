package com.example.agileus.ui.modulotareas.detalletareas

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.agileus.R
import com.example.agileus.models.DataTask
import com.example.agileus.ui.modulotareas.listenerstareas.TaskListListener
import java.lang.IllegalStateException
import java.util.*

class DialogoNivelBajo(private var listener: TaskListListener, var dataTask: DataTask) :
    DialogFragment() {

    private lateinit var observacionesD: String
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
            var btnEstado = vista.findViewById<Button>(R.id.btnCambiarEstadoD)

            Log.d("Mensaje", dataTask.toString())

            if (dataTask.estatus.equals("Pendiente")) {
                btnEstado.setText("Cambiar a Iniciada")
            } else if (dataTask.estatus.equals("Iniciada")) {
                btnEstado.setText("Cambiar a Revision")
            } else if (dataTask.estatus.equals("Revision")) {
                btnEstado.setText("En revision")
                btnEstado.isEnabled = false
            } else if (dataTask.estatus.equals("Terminada")) {
                btnEstado.setText("Tarea Terminada")
                btnEstado.isEnabled = false
            } else if (dataTask.estatus.equals("Cancelado")) {
                btnEstado.setText("Tarea Cancelada")
                btnEstado.isEnabled = false
            }


            nombreTarea = dataTask.titulo
            nombrePersonaD = dataTask.nombreEmisor
            prioridadD = dataTask.prioridad
//            // var txtEstatusD=args.tareas.estatus
            descripcionD = dataTask.descripcion
//            FechaInicioD = dataTask.fechaIni
//            FechaFinD = dataTask.fechaFin
            //  observacionesD = dataTask.observaciones

            txtNombreTareaD.text = nombreTarea
            txtNombrePersonaD.text = nombrePersonaD
            txtPrioridadD.text = prioridadD
            txtDescripcionD.text = descripcionD
////            txtFechaInicioD.text = FechaInicioD.toString()
////            txtFechaFinD.text = FechaFinD.toString()
            // txtObservacionesD.text = observacionesD
            this.dialog?.closeOptionsMenu()
            btnEstado.setOnClickListener {
                if (dataTask.estatus.equals("Pendiente")) {
                    dataTask.estatus = "Iniciada"
                } else if (dataTask.estatus.equals("Iniciada")) {
                    dataTask.estatus="Revision"
                }
                if (dataTask.estatus.equals("Revision")) {
                    btnEstado.isEnabled = false
                }
                detalleNivelBajoViewModel.actualizarEstatus(dataTask)
                this.dialog?.dismiss()
            }
            builder.setView(vista)

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }
}