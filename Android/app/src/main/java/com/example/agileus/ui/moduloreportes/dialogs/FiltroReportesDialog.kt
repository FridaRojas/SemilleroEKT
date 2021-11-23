package com.example.agileus.ui.moduloreportes.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.agileus.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class FiltroReportesDialog: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val vista = inflater.inflate(R.layout.dialog_filtro_reportes, null)

            val chipGroup = vista.findViewById<ChipGroup>(R.id.group)
            val chipDia = vista.findViewById<Chip>(R.id.chipFiltroDia)
            val chipSemana = vista.findViewById<Chip>(R.id.chipFiltroSemana)
            val chipMes = vista.findViewById<Chip>(R.id.chipFiltroMes)
            val chipAnio = vista.findViewById<Chip>(R.id.chipFiltroAnio)
            val chipCustom = vista.findViewById<Chip>(R.id.custom)



            chipGroup.setOnCheckedChangeListener { chipGroup, selectedId ->
                var hintStr = ""
                when (selectedId) {
                    R.id.chipFiltroDia -> {
                        hintStr = "Seleccionado es Dia"

                    }
                    R.id.chipFiltroSemana -> hintStr = "Seleccionado es Semana"
                    R.id.chipFiltroMes -> hintStr = "Seleccionado es Mes"
                    R.id.chipFiltroAnio -> hintStr = "Seleccionado es Año"
                    R.id.chipFiltroPersonalizado -> hintStr = "Seleccionado es Custom"
                    else -> hintStr = "Ningún chip seleccionado"
                }
                Toast.makeText(context, hintStr, Toast.LENGTH_SHORT).show()
            }



            /*

            for (i in 0 until chipGroup.childCount) {
                val chip = chipGroup.getChildAt(i) as Chip
                chip.isCheckable = chip.id != chipGroup.checkedChipId
                chip.isChecked = chip.id == chipGroup.checkedChipId
            }


            //Revision de funcionalidad de CHIP, no utilizado
            var lastCheckedId = View.NO_ID
            chipGroup.setOnCheckedChangeListener { group, checkedId ->
                if(checkedId == View.NO_ID) {
                    // User tried to uncheck, make sure to keep the chip checked
                    group.check(lastCheckedId)
                    return@setOnCheckedChangeListener
                }
                lastCheckedId = checkedId

                // New selection happened, do your logic here.
                //(...)
            }
             */
            builder.setView(vista)
                // Add action buttons
                .setPositiveButton("Guardar",
                    DialogInterface.OnClickListener { dialog, id ->
                        Toast.makeText(context, "${chipGroup.checkedChipId}", Toast.LENGTH_SHORT).show()
                        Log.d("Mensaje", chipGroup.checkedChipId.toString())
                        if (chipDia.isChecked){
                            Log.d("Mensaje", "PrimerID")
                        }
                    })
                .setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}