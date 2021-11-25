package com.example.agileus.ui.moduloreportes.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.util.*

import android.widget.NumberPicker
import android.widget.Toast
import com.example.agileus.R


class YearPickerDialogFragment(val listener: YearPickerDialogListener): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val vista = inflater.inflate(R.layout.dialog_picker_filtro_meses_anios, null)

            val cal: Calendar = Calendar.getInstance()
            val year: Int = cal.get(Calendar.YEAR)

            val yearPicker = vista.findViewById<NumberPicker>(R.id.picker_year_month)
            yearPicker.setMinValue(2000)
            yearPicker.setMaxValue(2100)
            yearPicker.setValue(year)

            builder.setView(vista)
                // Add action buttons
                .setPositiveButton("Aceptar",
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.onSelectedYear(yearPicker.value)
                    })
                .setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    interface YearPickerDialogListener{
        fun onSelectedYear(year: Int)
    }

}