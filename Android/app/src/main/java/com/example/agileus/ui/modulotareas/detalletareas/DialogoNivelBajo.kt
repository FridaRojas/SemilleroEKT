package com.example.agileus.ui.modulotareas.detalletareas

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
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

            nombreTarea = dataTask.titulo
            nombrePersonaD = dataTask.nombreReceptor
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
            dialog?.closeOptionsMenu()
            builder.setView(vista)
                .setPositiveButton(getString(R.string.BtnCambiarEstadoDialogo),
                    DialogInterface.OnClickListener { dialog, id ->


                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }
}