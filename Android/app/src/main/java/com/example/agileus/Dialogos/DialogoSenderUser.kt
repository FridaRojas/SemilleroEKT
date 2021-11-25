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
import com.example.agileus.ui.modulomensajeriabuzon.b.BuzonDetallesFragment
import com.example.agileus.ui.modulomensajeriabuzon.b.BuzonDetallesUserFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class DialogoSenderUser(val listener: BuzonDetallesUserFragment) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater            = requireActivity().layoutInflater
            val vista               = inflater.inflate(R.layout.mensaje_broadcasting_user, null)

            val Asunto          = vista.findViewById<TextInputEditText>(R.id.Asunto)
            val Mensaje         = vista.findViewById<TextInputEditText>(R.id.Mensajes)


//            val items = listOf("Option 1", "Option 2", "Option 3", "Option 4")///lista a consumir

  //          val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
            val Destinatario =vista.findViewById<TextInputEditText>(R.id.Responsable)
            Destinatario.isEnabled=false

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
                                    Buzon("","","Broadcast",Asunto.text.toString(),Mensaje.text.toString())
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