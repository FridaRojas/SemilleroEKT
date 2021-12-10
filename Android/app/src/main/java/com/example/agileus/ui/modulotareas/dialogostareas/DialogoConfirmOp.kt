package com.example.agileus.ui.modulotareas.dialogostareas

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.modulotareas.creartareas.CrearTareasViewModel
import com.example.agileus.models.Tasks
import com.example.agileus.ui.modulomensajeria.conversation.UserConversationViewModel

class DialogoConfirmOp(var tarea: Tasks) : DialogFragment(){

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
                        asignarTareaViewModel.postTarea(tarea)
                        // Enviar tarea a la conversacion grupal
                        // TODO: 08/12/2021  agregar id receptor desde sharedpreferences id_grupo
                        //val mensajeTareas = Message(
                            //Constantes.id,"618b05c12d3d1d235de0ade0","",
                          //"Se asigno la tarea: ${tarea.titulo} a ${tarea.nombreReceptor}",Constantes.finalDate)
                        //conversationviewModel.mandarMensaje(Constantes.idChat,mensajeTareas)
                        //Log.d("mensaje Tareas","$mensajeTareas")

                        val newFragment = DialogoAceptar("Tarea ${tarea.titulo} creada")
                        newFragment.show(
                            (activity as HomeActivity).supportFragmentManager,
                            "missiles"
                        )
                    })
                .setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener { dialog, id ->

                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }



}