package com.example.demoroom.dialogos

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.agileus.Models.Buzon
import com.example.agileus.R
import com.example.agileus.ui.modulomensajeriabuzon.b.BroadcasterListener
import com.google.android.material.textfield.TextInputLayout

class DialogoSenderBroadcast(val listener: BroadcasterListener) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater            = requireActivity().layoutInflater
            val vista               = inflater.inflate(R.layout.mensaje_broadcasting, null)

            val Asunto          = vista.findViewById<TextInputLayout>(R.id.Asunto)
            val Mensaje         = vista.findViewById<TextInputLayout>(R.id.Mensajes)

            val items = listOf("Option 1", "Option 2", "Option 3", "Option 4")///lista a consumir

            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
            val Destinatario =vista.findViewById<AutoCompleteTextView>(R.id.Responsable)
            Destinatario.setAdapter(adapter)

            builder.setView(vista)
                .setPositiveButton(
                    "Aceptar",
                    DialogInterface.OnClickListener { dialog, id ->
                        if(Asunto.toString().isEmpty() || Mensaje.toString().isEmpty() ) {
                            Toast.makeText(activity,
                                "",
                                Toast.LENGTH_LONG
                            ).show()
                        }else{
                        listener.mensajeBroadcasting(
                                Buzon("Broadcast",Destinatario.text.toString(),Asunto.toString(),Mensaje.toString())
                        )
                        }
                    })
                .setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}