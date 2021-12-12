package com.example.agileus.ui.modulotareas.dialogostareas

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.agileus.models.Message
import com.example.agileus.ui.modulotareas.creartareas.CrearTareasViewModel
import com.example.agileus.models.Tasks
import com.example.agileus.ui.modulotareas.listenerstareas.DialogoConfirmOpStatusListener
import com.example.agileus.utils.Constantes
import com.example.agileus.ui.modulomensajeria.conversation.UserConversationViewModel
import com.example.agileus.ui.modulotareas.listenerstareas.ProgressBarListener

class DialogoConfirmOp(var tarea: Tasks, var idEmisor:String, var idRecep:String,
                       val listener: DialogoConfirmOpStatusListener,
                       val listenerProgress: ProgressBarListener )
    : DialogFragment(), DialogoConfirmOpStatusListener{

    lateinit var conversationviewModel  : UserConversationViewModel         // ViewModel
    lateinit var asignarTareaViewModel  : CrearTareasViewModel          // ViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            asignarTareaViewModel = ViewModelProvider(this).get()                   // ViewModel
            conversationviewModel = ViewModelProvider(this).get()

            val builder = AlertDialog.Builder(it)

            builder.setMessage("Confirmar")
                // Add action buttons
                .setPositiveButton("Aceptar",
                    DialogInterface.OnClickListener { dialog, id ->
                        asignarTareaViewModel.postTarea(tarea , this)
                        listenerProgress.onAceptSelected()
                    })
                .setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener { dialog, id ->

                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onOpSuccessful() {
        // Enviar tarea al chat del receptor
        val mensajeTareas = Message(idEmisor,idRecep,"",
            "Se asigno la tarea: ${tarea.titulo} a ${tarea.nombreReceptor}",
            Constantes.finalDate)
        conversationviewModel.mandarMensaje(idEmisor,idRecep,mensajeTareas)
        Log.d("mensaje Tareas","$mensajeTareas")
        listener.onOpSuccessful()
    }

    override fun onOpFailure() {
        listener.onOpFailure()
    }


}