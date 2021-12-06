package com.example.agileus.ui.modulotareas.dialogostareas

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.agileus.R
import com.example.agileus.ui.modulotareas.listenerstareas.DialogoConfirmacionListener
import java.lang.ClassCastException

class DialogoTareaCreadaExitosamente() : DialogFragment() {

    internal lateinit var listeners: NoticeDialogListener

    interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

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
                        listeners.onDialogPositiveClick(this)
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listeners = context as NoticeDialogListener

        } catch (e: ClassCastException) {
            throw ClassCastException(
                (context.toString() +
                        " must implement NoticeDialogListener")
            )
        }
    }


}