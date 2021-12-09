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

class DialogoAceptar(var mensaje: String) :
    DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction

            val builder = AlertDialog.Builder(it)
            builder.setMessage(mensaje)
                .setPositiveButton(R.string.respAceptar,
                    DialogInterface.OnClickListener { dialog, id ->

                    })

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }
}