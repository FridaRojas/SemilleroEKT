package com.example.agileus.ui.moduloreportes.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.agileus.R
import com.example.agileus.config.MySharedPreferences
import com.example.agileus.utils.Constantes
import java.lang.Exception
import java.util.*


class UserStadisticPickerDialogFragment(val listener: UserStadistickPickerDialogListener):  DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val vista = inflater.inflate(R.layout.dialog_picker_filtro_meses_anios, null)

            //MySharedPreferences.empleadoUsuario[0].nombre
            var listaNombres = arrayListOf<String>()
            //listaNombres.add(MySharedPreferences.idUsuario)
            if(Constantes.empleadoUsuario.size == 0){
                Constantes.dataEmpleadoUsuario.forEach {
                    listaNombres.add(it.name)
                    Log.d("UserListDialogEmp", it.name)
                }
            }else{
                Constantes.empleadoUsuario.forEach {
                    listaNombres.add(it.name)
                    Log.d("UserListDialogData", it.name)
                }
            }
            /*
            if(Constantes.dataEmpleadoUsuario.size == 0){
                Constantes.empleadoUsuario.forEach {
                    listaNombres.add(it.name)
                    Log.d("UserListDialogEmp", it.name)
                }
            }else{
                Constantes.dataEmpleadoUsuario.forEach {
                    listaNombres.add(it.name)
                    Log.d("UserListDialogData", it.name)
                }
            }
             */

            val userPicker = vista.findViewById<NumberPicker>(R.id.picker_year_month)
            val txtTitulo = vista.findViewById<TextView>(R.id.txtDPFTitulo)
            txtTitulo.setText("Usuarios")
            userPicker.setMinValue(0)
            try {
                userPicker.setMaxValue(listaNombres.size-1)
                userPicker.setValue(0)
                userPicker.displayedValues = listaNombres.toTypedArray()
            }catch (ex:Exception){
                userPicker.setMaxValue(0)
                userPicker.setValue(0)
                userPicker.displayedValues = arrayOf(Constantes.idUsuario)
                Log.d("UserFilterError", ex.toString())
            }

            builder.setView(vista)
                // Add action buttons
                .setPositiveButton("Guardar",
                    DialogInterface.OnClickListener { dialog, id ->
                        listener.onUserSelected(userPicker.value)
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