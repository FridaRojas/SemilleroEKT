package com.example.agileus.ui.login.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment


class CerrarSesionDialog (val objeto : DialogoListen) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage("¿Desea salir de Agile Us?")
                .setPositiveButton("Sí",
                    DialogInterface.OnClickListener { dialog, id ->
                        objeto.siDisparar("Sesión Finalizada")
                    })
                .setNegativeButton("Cancelar", DialogInterface.OnClickListener { dialog, id ->
                    objeto.noDisparar("Opción cancelada")
                })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}









/*class RecuperaPasswordDialog (val listener:DialogoListen) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val vista = inflater.inflate(R.layout.formu_recupera_pass, null)

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
 */