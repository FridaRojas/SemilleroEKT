package com.example.agileus.ui.modulotareas.dialogostareas

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.agileus.R
import com.example.agileus.models.Message
import com.example.agileus.ui.modulotareas.creartareas.CrearTareasViewModel
import com.example.agileus.models.Tasks
import com.example.agileus.ui.modulotareas.listenerstareas.DialogoConfirmOpStatusListener
import com.example.agileus.utils.Constantes
import com.example.agileus.ui.modulomensajeria.conversation.UserConversationViewModel
import com.example.agileus.ui.modulotareas.listenerstareas.ProgressBarListener

class DialogoConfirmOp(var tarea: Tasks,
                       val listener: DialogoConfirmOpStatusListener,
                       val listenerProgress: ProgressBarListener )
    : DialogFragment(), DialogoConfirmOpStatusListener{

    lateinit var asignarTareaViewModel  : CrearTareasViewModel          // ViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            asignarTareaViewModel = ViewModelProvider(this).get()                   // ViewModel

            val builder = AlertDialog.Builder(it)

            builder.setMessage(getString(R.string.confirmar))
                // Add action buttons
                .setPositiveButton(getString(R.string.respAceptar),
                    DialogInterface.OnClickListener { dialog, id ->
                        asignarTareaViewModel.postTarea(tarea , this)
                        listenerProgress.onAceptSelected()
                    })
                .setNegativeButton(getString(R.string.respCancelar),
                    DialogInterface.OnClickListener { dialog, id ->

                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onOpSuccessful() {
        listener.onOpSuccessful()
    }

    override fun onOpFailure() {
        listener.onOpFailure()
    }


}