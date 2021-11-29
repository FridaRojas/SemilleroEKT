package com.example.agileus.ui.moduloreportes.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.agileus.R
import java.util.*


class UserStadisticPickerDialogFragment(val listener: UserStadistickPickerDialogListener):  DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val vista = inflater.inflate(R.layout.dialog_picker_filtro_meses_anios, null)

            val cal= Calendar.getInstance()
            val month = cal.get(Calendar.MONTH)

            val monthPicker = vista.findViewById<NumberPicker>(R.id.picker_year_month)
            val txtTitulo = vista.findViewById<TextView>(R.id.txtDPFTitulo)
            txtTitulo.setText("Usuarios")
            monthPicker.setMinValue(1)
            monthPicker.setMaxValue(3)
            monthPicker.setValue(1)

            monthPicker.displayedValues = arrayOf("Hijo 1", "Hijo 2", "Hijo 3")

            builder.setView(vista)
                // Add action buttons
                .setPositiveButton("Guardar",
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.onUserSelected(monthPicker.value)
                    })
                .setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    interface UserStadistickPickerDialogListener{
        fun onUserSelected(user: Int)
    }

}