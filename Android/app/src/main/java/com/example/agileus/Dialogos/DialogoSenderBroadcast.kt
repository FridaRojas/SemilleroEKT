package com.example.demoroom.dialogos

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.agileus.Models.Buzon
import com.example.agileus.R
import com.example.agileus.ui.modulomensajeriabuzon.b.BroadcasterListener

class DialogoSenderBroadcast(val listener: BroadcasterListener, val destinatario: String) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater            = requireActivity().layoutInflater
            val vista               = inflater.inflate(R.layout.mensaje_broadcasting, null)

            val Asunto        = vista.findViewById<EditText>(R.id.Asunto)
            val Mensaje         = vista.findViewById<EditText>(R.id.Mensajes)

            val Destinatario =vista.findViewById<TextView>(R.id.Responsable)
            Destinatario.setText(destinatario)

            builder.setView(vista)


                .setPositiveButton(
                    "Aceptar",
                    DialogInterface.OnClickListener { dialog, id ->
                        if(Asunto.text.toString().isEmpty() || Mensaje.text.toString().isEmpty() ) {
                            Toast.makeText(activity,
                                "",
                                Toast.LENGTH_LONG
                            ).show()
                        }else{
                        listener.mensajeBroadcasting(
                                Buzon("Broadcast",destinatario,Asunto.text.toString(),Mensaje.text.toString())
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