package com.example.agileus.ui.modulotareas.dialogostareas

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.agileus.R
import com.example.agileus.models.DataTask
import com.example.agileus.models.TaskUpdate
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.modulotareas.detalletareas.DetalleNivelAltoFragmentArgs
import com.example.agileus.ui.modulotareas.detalletareas.DetalleNivelAltoViewModel
import com.example.agileus.ui.modulotareas.listenerstareas.dialogoConfirmarListener
import com.example.agileus.webservices.dao.TasksDao
import java.lang.IllegalStateException
import java.util.*

class DialogoActualizarEstatus(var args: DataTask, var listener: dialogoConfirmarListener) :
    DialogFragment() {

    private lateinit var detalleNivelBajoViewModel: DetalleNivelAltoViewModel
    var taskDao: TasksDao

    init {
        taskDao = TasksDao()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            detalleNivelBajoViewModel =
                ViewModelProvider(this).get(DetalleNivelAltoViewModel::class.java)
            val builder = AlertDialog.Builder(it)
            var estat: String = ""
            if (args.estatus.equals("pendiente")) {
                estat = "iniciada"
            } else if (args.estatus.equals("iniciada")) {
                estat = "revision"
            }
            builder.setMessage("Desea actualizar estatus a $estat")
                .setPositiveButton(R.string.respAceptar,
                    DialogInterface.OnClickListener { dialog, id ->
                        if (args.estatus.equals("pendiente")) {
                            args.estatus = "iniciada"
                        } else if (args.estatus.equals("iniciada")) {
                            args.estatus = "revision"
                        }
                        detalleNivelBajoViewModel.actualizarEstatus(args, listener)

                    })
                .setNegativeButton(R.string.respCancelar,
                    DialogInterface.OnClickListener { dialog, id ->
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }
}