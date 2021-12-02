package com.example.agileus.ui.modulotareas.dialogostareas

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.example.agileus.ui.modulotareas.listenerstareas.DialogoFechaListener
import java.util.*

class EdtFecha(val listenerFormularioCrear: DialogoFechaListener, b: Int) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    var bandera = b

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        var calendar = Calendar.getInstance()
        var anio = calendar.get(Calendar.YEAR)
        var mes = calendar.get(Calendar.MONTH)
        var dia = calendar.get(Calendar.DAY_OF_MONTH)
        var picker = DatePickerDialog(activity as Context, this, anio, mes, dia)

        picker.datePicker.minDate = calendar.timeInMillis   // validacion de minima fecha
        return picker
    }

    override fun onDateSet(p0: DatePicker?, anio: Int, mes: Int, dia: Int) {
        if (bandera == 1) {
            listenerFormularioCrear.onDateInicioSelected(anio, mes, dia)
        } else if (bandera == 2) {
            listenerFormularioCrear.onDateFinSelected(anio, mes, dia)
        }
    }
}
