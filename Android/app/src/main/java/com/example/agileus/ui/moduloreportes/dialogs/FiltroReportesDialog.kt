package com.example.agileus.ui.moduloreportes.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.agileus.R
import com.example.agileus.R.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.*
import kotlin.collections.ArrayList

class FiltroReportesDialog(): DialogFragment(),
        DatePickerFiltroReportesDialogFragment.DatePickerFiltroReportesDialogListener,
        MonthPickerDialogFragment.MonthPickerDialogListener,
        YearPickerDialogFragment.YearPickerDialogListener
    {
    private lateinit var txtFechaInicio: TextView
    private lateinit var txtFechaFin: TextView
    private lateinit var txtMes: TextView
    private lateinit var txtAnio: TextView
    private var dateSelected = 0
    private var opcionFiltro = 0

    val actualCal= Calendar.getInstance()
    val actualDay = actualCal.get(Calendar.DAY_OF_MONTH)
    val actualMonth = actualCal.get(Calendar.MONTH) + 1
    val actualYear = actualCal.get(Calendar.YEAR)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val vista = inflater.inflate(layout.dialog_filtro_reportes, null)

            val txtInicio = vista.findViewById<TextView>(R.id.txtFechaInicio)
            val txtFin = vista.findViewById<TextView>(R.id.txtFechaFin)
            txtFechaInicio = vista.findViewById(R.id.txtNumeroFechaInicio)
            txtFechaFin = vista.findViewById(R.id.txtNumeroFechaFin)
            txtMes = vista.findViewById(R.id.txtFiltroMes)
            txtAnio = vista.findViewById(R.id.txtFiltroAnio)

            val chipGroup = vista.findViewById<ChipGroup>(R.id.group)
            val chipDia = vista.findViewById<Chip>(R.id.chipFiltroDia)
            val chipSemana = vista.findViewById<Chip>(R.id.chipFiltroSemana)
            val chipMes = vista.findViewById<Chip>(R.id.chipFiltroMes)
            val chipAnio = vista.findViewById<Chip>(R.id.chipFiltroAnio)
            val chipCustom = vista.findViewById<Chip>(R.id.custom)

            txtInicio.setText("Dia:")
            val fechaActual = dmyFormatoFecha(actualDay, actualMonth-1, actualYear)
            txtFechaInicio.setText(formatoDiaSelected(actualDay, actualMonth, actualYear, fechaActual))
            txtFechaFin.setText(fechaActual)
            txtFin.visibility =View.GONE
            txtFechaFin.visibility =View.GONE
            chipSemana.visibility =View.GONE
            txtMes.setText(mesDelAnio(actualDay, actualMonth, actualYear))
            txtMes.visibility =View.GONE
            txtAnio.visibility =View.GONE

            chipGroup.setOnCheckedChangeListener { chipGroup, selectedId ->
                when (selectedId) {
                    R.id.chipFiltroDia -> {
                        opcionFiltro = 0
                        txtFin.visibility =View.GONE
                        txtFechaFin.visibility =View.GONE
                        txtFechaInicio.visibility = View.VISIBLE
                        txtMes.visibility =View.GONE
                        txtAnio.visibility =View.GONE
                        txtFechaInicio.setText(formatoDiaSelected(actualDay, actualMonth, actualYear, fechaActual))

                        txtInicio.setText("Dia:")
                    }
                    R.id.chipFiltroSemana -> {
                        opcionFiltro = 1
                        txtFin.visibility =View.GONE
                        txtFechaFin.visibility =View.GONE
                        txtInicio.setText("Semana:")
                    }
                    R.id.chipFiltroMes -> {
                        opcionFiltro = 2
                        txtFin.visibility =View.GONE
                        txtFechaFin.visibility =View.GONE
                        txtFechaInicio.visibility =View.GONE
                        txtAnio.visibility =View.GONE
                        txtInicio.setText("Mes:")
                        txtMes.visibility =View.VISIBLE

                    }
                    R.id.chipFiltroAnio -> {
                        opcionFiltro = 3
                        txtFin.visibility =View.GONE
                        txtFechaFin.visibility =View.GONE
                        txtFechaInicio.visibility =View.GONE
                        txtMes.visibility =View.GONE
                        txtAnio.visibility =View.VISIBLE
                        txtInicio.setText("Año:")
                        txtAnio.setText(actualYear.toString())
                    }
                    R.id.chipFiltroPersonalizado -> {
                        opcionFiltro = 4
                        txtFin.visibility =View.VISIBLE
                        txtFechaFin.visibility =View.VISIBLE
                        txtFechaInicio.visibility = View.VISIBLE
                        txtMes.visibility =View.GONE
                        txtAnio.visibility =View.GONE
                        txtInicio.setText("Fecha de inicio:")
                        txtFechaInicio.setText(fechaActual)
                    }
                    else -> opcionFiltro = 5
                }
                //Toast.makeText(context, opcionFiltro.toString(), Toast.LENGTH_SHORT).show()
            }

            txtFechaInicio.setOnClickListener {
                dateSelected = 0
                val newFragment = DatePickerFiltroReportesDialogFragment(this)
                newFragment.show(requireActivity().supportFragmentManager, "Filtro de Reportes")
            }
            txtFechaFin.setOnClickListener {
                dateSelected = 1
                val newFragment = DatePickerFiltroReportesDialogFragment(this)
                newFragment.show(requireActivity().supportFragmentManager, "Filtro de Reportes")
            }
            txtMes.setOnClickListener {
                val newFragment = MonthPickerDialogFragment(this)
                newFragment.show(requireActivity().supportFragmentManager, "Filtro de Reportes")
            }
            txtAnio.setOnClickListener {
                val newFragment = YearPickerDialogFragment(this)
                newFragment.show(requireActivity().supportFragmentManager, "Filtro de Reportes")
            }

            builder.setView(vista)
                // Add action buttons
                .setPositiveButton("Aceptar",
                    DialogInterface.OnClickListener { dialog, id ->
                        //Toast.makeText(context, "${chipGroup.checkedChipId}", Toast.LENGTH_SHORT).show()
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

    interface filtroReportesDialogListener{
        //fun onDateSelected(anio:Int,mes:Int,dia:Int)
    }

    override fun onDateFiltroReportesSelected(anio: Int, mes: Int, dia: Int){
        val currentDate = dmyFormatoFecha(dia, mes, anio)//= sdf.format(d)

        if (dateSelected == 0){
            txtFechaInicio.setText(formatoDiaSelected(dia, mes+1, anio, currentDate))
            if(opcionFiltro == 4){
                txtFechaInicio.setText(currentDate)
            }
        }else{
            txtFechaFin.setText(currentDate)
        }
    }

    fun nombreDiaDelAnio(date:Int,  month: Int, year: Int): String{
        val dt = Date(year-1900, month-1, date)
        val dateFormat = SimpleDateFormat("EEEE", Locale.forLanguageTag("Es"))
        val asDay: String = dateFormat.format(dt)

        return asDay
    }


    fun mesDelAnio( date:Int, month: Int, year: Int): String{
        val dt = Date(year-1900, month-1, date)
        val dateFormat = SimpleDateFormat("MMMM", Locale.forLanguageTag("Spanish"))
        val asMonth: String = dateFormat.format(dt)

        return asMonth
    }

    fun dmyFormatoFecha(dia: Int, mes: Int, anio: Int):String{
        var dt = Date(anio-1900, mes, dia)
        val sdf = SimpleDateFormat("dd/MM/YYYY", Locale.US)
        val currentDate = sdf.format(dt)
        return currentDate
    }

    fun formatoDiaSelected(dia: Int, mes: Int, anio: Int, currentDF: String): String{
        return "${nombreDiaDelAnio(dia, mes, anio)}: ${currentDF} "
    }

    override fun onMonthSelected(mes: Int) {
        txtMes.setText(mesDelAnio(actualDay, mes, actualYear))
    }

    override fun onSelectedYear(year: Int) {
        txtAnio.setText(year.toString())
    }


        /*

    fun posicionPrimerDiaDelAnio(year: Int): Int{
        val asDay = nombreDiaDelAnio(year, 0, 1)
        val posDia = posDiaSemana(asDay, listaDiasDeLaSemana())
        return posDia
    }

    fun posDiaSemana(dia:String, diasDeLaSemana: ArrayList<String>):Int{
        var aux = 0
        var pos = 0
        diasDeLaSemana.forEach {
            if (dia == it){
                pos = aux
            }
            aux += 1
        }
        return pos
    }



    fun semanasDelAnio(year: Int, day: Int): ArrayList<String>{
        var listaSemanas  = arrayListOf<String>()
        var dt = Date(year-1900, 0, 1)
        var dateFormat = SimpleDateFormat("w", Locale.US)
        var asWeeks: String = dateFormat.format(dt)
        var isBisiesto = false
        var semanasTotales = 52

        if(year%4 == 0 && (year%100 != 0 || year%400 == 0)){
            isBisiesto = true
            semanasTotales = 53
        }else

        for(weeks in 0..semanasTotales){
            dt = Date(2000-1900, 1, 1)
            dateFormat = SimpleDateFormat("w", Locale.US)
            asWeeks = dateFormat.format(dt)
            listaSemanas.add(asWeeks)
        }

        Toast.makeText(context, asWeeks, Toast.LENGTH_SHORT).show()
        return listaSemanas
    }

    fun listaDiasDeLaSemana(): ArrayList<String>{
        val listaDias  = arrayListOf<String>()
        for(date in 23..29){
            listaDias.add(nombreDiaDelAnio(2021-1900, 11, date))
        }
        return listaDias
    }



    fun listaMesesDelAnio(): ArrayList<String>{
        val listaMeses  = arrayListOf<String>()
        var dt = Date(2000-1900, 0, 1)
        var dateFormat = SimpleDateFormat("MMMM", Locale.forLanguageTag("Spanish"))
        var asMonth: String = dateFormat.format(dt)
        for(month in 0..11){
            dt = Date(2000-1900, month, 1)
            dateFormat = SimpleDateFormat("MMMM", Locale.forLanguageTag("Spanish"))
            asMonth = dateFormat.format(dt)
            listaMeses.add(asMonth)
        }
        Log.d("Lista", listaMeses.toString())
        return listaMeses
    }
         */


}

