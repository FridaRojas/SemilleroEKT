package com.lalo.room_reto_training.dialogos

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.agileus.ui.modulomensajeriabuzon.b.BroadcasterListener

class SeleccionSimpleDialogFragment(val listener: BroadcasterListener): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val TipodeUsuario=arrayOf<String>("Ventas","Compras","Ti","Soporte","General")
        return activity?.let {
            val selectedItems = ArrayList<Int>() // Where we track the selected items
            var seleccionado=TipodeUsuario[0]
            val builder = AlertDialog.Builder(it)
            // Set the dialog title
            builder.setTitle("Destinario Broadcast")
                .setSingleChoiceItems(TipodeUsuario, 4,
                    DialogInterface.OnClickListener { dialog, which->
                        seleccionado=TipodeUsuario[which]
                    })

                .setPositiveButton("ok",
                    DialogInterface.OnClickListener { dialog, id ->

                        listener.Tipodeusuario(seleccionado)

                    })
                .setNegativeButton("no",
                    DialogInterface.OnClickListener { dialog, id ->

                    })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}