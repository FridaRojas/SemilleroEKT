package com.example.agileus.ui.modulotareas.dialogostareas

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.agileus.R
import com.example.agileus.ui.modulotareas.detalletareas.DetalleNivelAltoFragmentArgs
import com.example.agileus.ui.modulotareas.detalletareas.DetalleNivelAltoViewModel
import com.example.agileus.webservices.dao.TasksDao
import java.lang.IllegalStateException
import java.util.*

class DialogoAceptar(var args: DetalleNivelAltoFragmentArgs) :
    DialogFragment() {

    private lateinit var observacionesD: String
    private lateinit var fechaFinD: Date
    private lateinit var fechaInicioD: Date
    private lateinit var descripcionD: String
    private lateinit var prioridadD: String
    private lateinit var nombrePersonaD: String
    private lateinit var nombreTarea: String
    private lateinit var detalleNivelAltoViewModel: DetalleNivelAltoViewModel

    var taskDao: TasksDao
    var resp: Boolean = false

    init {
        taskDao = TasksDao()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            detalleNivelAltoViewModel =
                ViewModelProvider(this).get(DetalleNivelAltoViewModel::class.java)
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Desea eliminar tarea ${args.tareas.titulo}?")
                .setPositiveButton(R.string.respAceptar,
                    DialogInterface.OnClickListener { dialog, id ->
                        // FIRE ZE MISSILES!
                        detalleNivelAltoViewModel.cancelarTarea(args)

                    })
                .setNegativeButton(R.string.respCancelar,
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }
}