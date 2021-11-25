package com.example.agileus.ui.modulotareas.dialogostareas

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import com.example.agileus.R
import com.example.agileus.models.DataTask
import com.example.agileus.ui.modulotareas.detalletareas.DetalleNivelAltoViewModel
import com.example.agileus.ui.modulotareas.listenerstareas.TaskListListener
import com.example.agileus.webservices.dao.TasksDao
import java.lang.IllegalStateException
import java.util.*

class DialogoAceptar(private var listener: TaskListListener, var dataTask: DataTask) :
    DialogFragment() {

    private lateinit var observacionesD: String
    private lateinit var fechaFinD: Date
    private lateinit var fechaInicioD: Date
    private lateinit var descripcionD: String
    private lateinit var prioridadD: String
    private lateinit var nombrePersonaD: String
    private lateinit var nombreTarea: String

    var taskDao: TasksDao

    init {
        taskDao = TasksDao()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            val vista = inflater.inflate(R.layout.dialog_nivel_bajo, null)

            builder.setView(vista)
                .setPositiveButton("Aceptar",
                    DialogInterface.OnClickListener { dialog, id ->
//                        try {
//                            viewModelScope.launch {
//                                taskDao.cancelTask(dataTask)
//                            }
//                        } catch (ex: Exception) {
//                            Log.e(DetalleNivelAltoViewModel::class.simpleName.toString(), ex.message.toString())
//                        }
                    })
                .setNegativeButton("Cancelar",
                DialogInterface.OnClickListener { dialog, which ->  })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }
}