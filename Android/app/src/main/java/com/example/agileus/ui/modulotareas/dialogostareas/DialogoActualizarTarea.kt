package com.example.agileus.ui.modulotareas.dialogostareas

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.agileus.R
import com.example.agileus.models.TaskUpdate
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.modulotareas.detalletareas.DetalleNivelAltoViewModel
import com.example.agileus.ui.modulotareas.listenerstareas.dialogoConfirmarListener
import com.example.agileus.webservices.dao.TasksDao
import java.lang.IllegalStateException

class DialogoActualizarTarea(
    var args: TaskUpdate,
    var idTarea: String,
    var idUsuario: String,
    var listener: dialogoConfirmarListener
) :
    DialogFragment() {

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
            builder.setMessage("Desea actualizar tarea ${args.titulo}?")
                .setPositiveButton(R.string.respAceptar,
                    DialogInterface.OnClickListener { dialog, id ->
                        detalleNivelAltoViewModel.editarTarea(args, idTarea, idUsuario, listener)
                    })
                .setNegativeButton(R.string.respCancelar,
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }


}