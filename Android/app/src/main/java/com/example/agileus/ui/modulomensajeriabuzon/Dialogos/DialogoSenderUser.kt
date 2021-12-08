package com.example.agileus.ui.modulomensajeriabuzon.Dialogos

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.agileus.models.Buzon
import com.example.agileus.R
import com.example.agileus.models.MsgBodyUser
import com.example.agileus.ui.modulomensajeriabuzon.BuzonUser.BuzonDetallesUserFragment
import com.example.agileus.ui.modulomensajeriabuzon.Listeners.UserBuzonListener
import com.google.android.material.textfield.TextInputEditText

class DialogoSenderUser(val listener: UserBuzonListener) : DialogFragment() {

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
                    "Aceptar"
                ) { _, _ ->
                    if (Asunto.toString().isEmpty() || Mensaje.toString().isEmpty()) {
                        Toast.makeText(
                            activity,
                            "",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        listener.mensajeBroadcasting1(
                            MsgBodyUser(Asunto.text.toString(), Mensaje.text.toString(), "123")
                        )
                    }
                }
                .setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}