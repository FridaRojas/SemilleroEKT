package com.example.agileus.ui.modulomensajeriabuzon.Dialogos

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.agileus.R
import com.example.agileus.models.MensajeBodyBroadcaster
import com.example.agileus.ui.modulomensajeriabuzon.Listeners.BroadcasterListener
import com.google.android.material.textfield.TextInputEditText

import android.widget.AutoCompleteTextView
import com.example.agileus.utils.Constantes.broadlist


class DialogoSenderBroadcast(val listener: BroadcasterListener, val lista: ArrayList<String>) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater            = requireActivity().layoutInflater
            val vista               = inflater.inflate(R.layout.mensaje_broadcasting, null)

            val Asunto          = vista.findViewById<TextInputEditText>(R.id.Asunto)
            val Mensaje         = vista.findViewById<TextInputEditText>(R.id.Mensajes1)



            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, lista)
            val Destinatario =vista.findViewById<AutoCompleteTextView>(R.id.Responsable)
            val Rolx =vista.findViewById<TextInputEditText>(R.id.txtRol)




            Destinatario.setAdapter(adapter)

            if(vista.isFocusable == true ){
                hideSoftKeyboard(activity as Activity)
            }

            builder.setView(vista)
                .setPositiveButton(
                    "Aceptar",
                    DialogInterface.OnClickListener { dialog, id ->
                        if(Asunto.toString().length<5 || Mensaje.toString().length<5 ) {
                            Toast.makeText(activity,
                                "Accion no permitida",
                                Toast.LENGTH_LONG
                            ).show()
                        }else {
                            var mensaje="Asunto : "+Asunto.text.toString()+"| Mensaje : "+Mensaje.text.toString()
                            listener.mensajeBroadcasting(
                                MensajeBodyBroadcaster(
                                    "2000-01-01T00:00:00.000+00:00",
                                    broadlist,
                                    Destinatario.text.toString(),
                                    mensaje
                                ))
                        }
                    })
                .setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager: InputMethodManager = activity.getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                activity.currentFocus!!.windowToken,
                0
            )
        }
    }
}