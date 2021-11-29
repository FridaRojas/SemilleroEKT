package com.example.agileus.Dialogos

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.agileus.R


//quite de passdialog(val listener: DialogListenerRecuperaPass)
class RecuperaPassDialog(val listener:DialogoListener) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val vista = inflater.inflate(R.layout.formulario_recupera_pass, null)

            val txtCorreo = vista.findViewById<EditText>(R.id.txtEmail)
            val txtPassword = vista.findViewById<EditText>(R.id.txtContraseña)

            builder.setView(vista)
                // Add action buttons
                .setPositiveButton("Recuperar Contraseña",
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.editRecuperarPassword(txtCorreo.text.toString(), txtPassword.text.toString())
                    })
                .setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}