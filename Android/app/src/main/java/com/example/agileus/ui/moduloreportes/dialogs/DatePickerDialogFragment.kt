package com.example.agileus.ui.moduloreportes.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFiltroReportesDialogFragment(val listener: DatePickerFiltroReportesDialogListener): DialogFragment(), DatePickerDialog.OnDateSetListener{

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        var anio = calendar.get(Calendar.YEAR)
        var mes =  calendar.get(Calendar.MONTH)
        var dia =  calendar.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(activity as Context,this,anio,mes,dia)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener.onDateFiltroReportesSelected(year,month,dayOfMonth)
    }

    interface DatePickerFiltroReportesDialogListener{
        fun onDateFiltroReportesSelected(anio:Int,mes:Int,dia:Int)
    }

}