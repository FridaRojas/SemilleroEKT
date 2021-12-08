package com.example.agileus.ui.modulotareas.dialogostareas

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.agileus.ui.modulotareas.listenerstareas.DialogoConfirmacionListener

class DialogoConfirmOp(val listener: DialogoConfirmacionListener) : DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            //val inflater = requireActivity().layoutInflater;
            //val vista = inflater.inflate(R.layout.dialogo_tarea_creada, null)

            builder.setMessage("Confirmar")
                // Add action buttons
                .setPositiveButton("Aceptar",
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.onConfirmOper()
                    })
                .setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener { dialog, id ->

                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }



}