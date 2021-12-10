package com.example.agileus.ui.moduloreportes.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.agileus.R
import org.w3c.dom.Text
import java.util.*

class MonthPickerDialogFragment(val listener: MonthPickerDialogListener):  DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val vista = inflater.inflate(R.layout.dialog_picker_filtro_meses_anios, null)

            val cal= Calendar.getInstance()
            val month = cal.get(Calendar.MONTH)

            val monthPicker = vista.findViewById<NumberPicker>(R.id.picker_year_month)
            val txtTitulo = vista.findViewById<TextView>(R.id.txtDPFTitulo)
            txtTitulo.setText("Mes")
            monthPicker.setMinValue(1)
            monthPicker.setMaxValue(12)
            monthPicker.setValue(month+1)

            //monthPicker.displayedValues = arrayOf(R.array.months_array.toString())
            //monthPicker.displayedValues = arrayOf(R.array.months_array)

            monthPicker.displayedValues =  arrayOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre" )

            builder.setView(vista)
                // Add action buttons
                .setPositiveButton("Guardar",
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.onMonthSelected(monthPicker.value)
                    })
                .setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    interface MonthPickerDialogListener{
        fun onMonthSelected(mes: Int)
    }

}