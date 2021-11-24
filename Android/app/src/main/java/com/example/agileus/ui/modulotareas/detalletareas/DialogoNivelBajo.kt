package com.example.agileus.ui.modulotareas.detalletareas

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.example.agileus.R
import com.example.agileus.ui.modulotareas.listenerstareas.TaskDialogListener
import com.example.agileus.ui.modulotareas.listenerstareas.TaskListListener
import java.lang.IllegalStateException
import java.util.*

class DialogoNivelBajo(private var listener: TaskListListener) : DialogFragment() {

    private lateinit var ObservacionesD: String
    private lateinit var FechaFinD: Date
    private lateinit var FechaInicioD: Date
    private lateinit var DescripcionD: String
    private lateinit var PrioridadD: String
    private lateinit var NombrePersonaD: String
    private lateinit var nombreTarea: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;
            val vista = inflater.inflate(R.layout.dialog_nivel_bajo, null)
            var txtNombreTareaD = vista.findViewById<TextView>(R.id.txtNombreTareaD)
            var txtNombrePersonaD = vista.findViewById<TextView>(R.id.txtNombrePersonaD)
            var txtPrioridadD = vista.findViewById<TextView>(R.id.txtPrioridadD)
            var txtDescripcionD = vista.findViewById<TextView>(R.id.txtDescripcionD)
            var txtFechaInicioD = vista.findViewById<TextView>(R.id.txtFechaInicioD)
            var txtFechaFinD = vista.findViewById<TextView>(R.id.txtFechaFinD)
            var txtObservacionesD = vista.findViewById<TextView>(R.id.txtObservacionesD)
//            val txtPassword = vista.findViewById<EditText>(R.id.password)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout

            val args: DialogoNivelBajoArgs by navArgs()
//
//            nombreTarea = args.tareas.titulo
//            NombrePersonaD = args.tareas.titulo
//            PrioridadD = args.tareas.prioridad
            // var txtEstatusD=args.tareas.estatus
//            DescripcionD = args.tareas.descripcion
//            FechaInicioD = args.tareas.fechaIni
//            FechaFinD = args.tareas.fechaFin
//            ObservacionesD = args.tareas.observaciones
//
//            txtNombreTareaD.text = nombreTarea
//            txtNombrePersonaD.text = NombrePersonaD
//            txtPrioridadD.text = PrioridadD
//            txtDescripcionD.text = DescripcionD
//            txtFechaInicioD.text = FechaInicioD.toString()
//            txtFechaFinD.text = FechaFinD.toString()
//            txtObservacionesD.text = ObservacionesD


            builder.setView(inflater.inflate(R.layout.dialog_nivel_bajo, null))
                // Add action buttons
                .setPositiveButton(getString(R.string.BtnCambiarEstadoDialogo),
                    DialogInterface.OnClickListener { dialog, id ->
                        // sign in the user ...
//                        listener.onSignInSuccess(
//                            txtUser.text.toString(),
//                            txtPassword.text.toString()
//                        )
                    })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }
}