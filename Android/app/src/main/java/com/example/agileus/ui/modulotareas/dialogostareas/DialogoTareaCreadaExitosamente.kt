package com.example.agileus.ui.modulotareas.dialogostareas

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.agileus.R
import com.example.agileus.ui.modulotareas.listenerstareas.DialogoConfirmacionListener

class DialogoTareaCreadaExitosamente (val listener: DialogoConfirmacionListener ) : DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;
            val vista = inflater.inflate(R.layout.dialogo_tarea_creada, null)

            builder.setView(inflater.inflate(R.layout.dialogo_tarea_creada, null))
                // Add action buttons
                .setPositiveButton("Aceptar",
                    DialogInterface.OnClickListener { dialog, id ->

                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }



}