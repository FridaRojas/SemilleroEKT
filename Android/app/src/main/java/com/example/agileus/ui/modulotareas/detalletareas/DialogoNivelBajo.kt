package com.example.agileus.ui.modulotareas.detalletareas

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.agileus.R
import java.lang.IllegalStateException

class DialogoNivelBajo : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;
//            val vista = inflater.inflate(R.layout.dialog_signin, null)
//            val txtUser = vista.findViewById<EditText>(R.id.username)
//            val txtPassword = vista.findViewById<EditText>(R.id.password)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog_nivel_bajo, null))
                // Add action buttons
                .setPositiveButton(getString(R.string.BtnCambiarEstadoDialogo),
                    DialogInterface.OnClickListener { dialog, id ->
                        // sign in the user ...
//                        listener.onSignInSuccess(
//                            txtUser.text.toString(),
//                            txtPassword.text.toString()
//                        )
                    })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }
}