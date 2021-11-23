package com.example.agileus.ui.modulotareas.dialogostareas

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.example.agileus.ui.modulotareas.listenerstareas.DialogosTareasListener
import java.util.*

class EdtFinFecha (val listener: DialogosTareasListener) : DialogFragment(), DatePickerDialog.OnDateSetListener{

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val anio = calendar.get(Calendar.YEAR)
        val mes = calendar.get(Calendar.MONTH)
        val dia = calendar.get(Calendar.DAY_OF_MONTH)

        val picker = DatePickerDialog(activity as Context,this, anio,mes,dia )
        picker.datePicker.minDate = calendar.timeInMillis // validacion de minima fecha es hoy
        return picker
    }

    override fun onDateSet(p0: DatePicker?, anio: Int, mes: Int, dia: Int) {
        listener.onDateFinSelected(anio, mes, dia)
    }

}