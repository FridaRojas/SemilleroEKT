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
import com.example.agileus.config.MySharedPreferences
import com.example.agileus.config.MySharedPreferences.reportesGlobales.empleadoUsuario
import com.example.agileus.config.MySharedPreferences.reportesGlobales.idUsuario
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.*

class FiltroReportesDialog(val listener: FiltroReportesDialogListener): DialogFragment(),
        DatePickerFiltroReportesDialogFragment.DatePickerFiltroReportesDialogListener,
        MonthPickerDialogFragment.MonthPickerDialogListener,
        YearPickerDialogFragment.YearPickerDialogListener,
        UserStadisticPickerDialogFragment.UserStadistickPickerDialogListener{
    private lateinit var txtFechaInicio: TextView
    private lateinit var txtFechaFin: TextView
    private lateinit var txtDia: TextView
    private lateinit var txtMes: TextView
    private lateinit var txtAnio: TextView
    private lateinit var txtTitulo: TextView
    private lateinit var txtUsuario: TextView
    private lateinit var iniStringDate: String
    private lateinit var endStringDate: String
    private var dateSelected = 0
    private var opcionFiltro = 0
    private var userIdSelected = MySharedPreferences.idUsuarioEstadisticas

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
            txtTitulo = vista.findViewById(R.id.txtFiltroNombre)
            txtDia = vista.findViewById(R.id.txtFiltroDia)
            txtUsuario = vista.findViewById(R.id.txtFiltroUsuario)

            val chipGroup = vista.findViewById<ChipGroup>(R.id.group)

            //txtTitulo.setText(MySharedPreferences.idUsuario)
            txtTitulo.setText("Filtrar")
            txtUsuario.setText(MySharedPreferences.idUsuarioEstadisticas)
            txtInicio.setText("Dia:")
            val fechaActual = dmyFormatoFecha(actualDay, actualMonth-1, actualYear)
            txtFechaInicio.setText(fechaActual)
            txtFechaFin.setText(fechaActual)
            txtFin.visibility =View.GONE
            txtFechaFin.visibility =View.GONE
            txtMes.setText(mesDelAnio(1, actualMonth, actualYear))
            txtMes.visibility =View.GONE
            txtAnio.visibility =View.GONE
            txtFechaInicio.visibility =View.GONE
            txtDia.setText(formatoDiaSelected(actualDay, actualMonth, actualYear, fechaActual))
            resRangeValues()

            chipGroup.setOnCheckedChangeListener { chipGroup, selectedId ->
                when (selectedId) {
                    R.id.chipFiltroDia -> {
                        opcionFiltro = 1
                        txtFin.visibility =View.GONE
                        txtFechaFin.visibility =View.GONE
                        txtFechaInicio.visibility =View.GONE
                        txtMes.visibility =View.GONE
                        txtAnio.visibility =View.GONE
                        txtFechaFin.setText(fechaActual)
                        resRangeValues()

                        txtInicio.setText("Dia:")
                        txtDia.visibility = View.VISIBLE
                        txtDia.setText(formatoDiaSelected(actualDay, actualMonth, actualYear, fechaActual))

                    }
                    R.id.chipFiltroMes -> {
                        opcionFiltro = 2
                        txtFin.visibility =View.GONE
                        txtFechaFin.visibility =View.GONE
                        txtFechaInicio.visibility =View.GONE
                        txtAnio.visibility =View.GONE
                        txtDia.visibility = View.GONE
                        txtFechaFin.setText(dmyFormatoFecha(1, actualMonth-1, actualYear))
                        resRangeValues()

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
                        txtDia.visibility = View.GONE
                        txtFechaFin.setText(dmyFormatoFecha(1, 0, actualYear))
                        resRangeValues()

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
                        txtDia.visibility = View.GONE
                        resRangeValues()

                        txtInicio.setText("Fecha de inicio:")
                        txtFechaInicio.setText(fechaActual)
                        txtFechaFin.setText(fechaActual)
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

            txtDia.setOnClickListener {
                dateSelected = 1
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
            txtUsuario.setOnClickListener {
                val newFragment = UserStadisticPickerDialogFragment(this)
                newFragment.show(requireActivity().supportFragmentManager, "Filtro de Reportes")
            }

            builder.setView(vista)
                .setPositiveButton("Aceptar",
                    DialogInterface.OnClickListener { dialog, id ->

                        MySharedPreferences.opcionFiltro = opcionFiltro
                        MySharedPreferences.fechaIniCustomEstadisticas = iniStringDate
                        MySharedPreferences.fechaEstadisticas = endStringDate
                        MySharedPreferences.idUsuarioEstadisticas = userIdSelected

                        //Toast.makeText(context, "ini: $iniStringDate, end: $endStringDate", Toast.LENGTH_LONG).show()
                        //Log.e("Filtro", "ini: $iniStringDate, end: $endStringDate")
                        listener.onDateFilterSelected()

                    })
                .setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    interface FiltroReportesDialogListener{
        fun onDateFilterSelected()
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
        val sdf = SimpleDateFormat("dd/MM/y", Locale.US)
        val currentDate = sdf.format(dt)
        return currentDate
    }

    fun dataBaseFormatoFecha(dia: Int, mes: Int, anio: Int):String{
        var dt = Date(anio-1900, mes, dia)
        val sdf = SimpleDateFormat("y-MM-dd", Locale.US)
        val currentDate = sdf.format(dt)+"T00:00:00.000+00:00"
        return currentDate
    }

    fun formatoDiaSelected(dia: Int, mes: Int, anio: Int, currentDF: String): String{
        return "${nombreDiaDelAnio(dia, mes, anio)}: ${currentDF} "
    }

    override fun onDateFiltroReportesSelected(anio: Int, mes: Int, dia: Int){
        val currentDate = dmyFormatoFecha(dia, mes, anio)//= sdf.format(d)
        txtDia.setText(formatoDiaSelected(dia, mes+1, anio, currentDate))

        if (dateSelected == 0){
            txtFechaInicio.setText(formatoDiaSelected(dia, mes, anio, currentDate))
            if(opcionFiltro == 4){
                txtFechaInicio.setText(currentDate)
                iniStringDate = dataBaseFormatoFecha(dia, mes, anio)
            }
        }else{
            txtFechaFin.setText(currentDate)
            endStringDate = dataBaseFormatoFecha(dia, mes, anio)
        }
    }

    override fun onMonthSelected(mes: Int) {
        txtMes.setText(mesDelAnio(actualDay, mes, actualYear))
        txtFechaFin.setText(dmyFormatoFecha(1, mes-1, actualYear))
        endStringDate = dataBaseFormatoFecha(1, mes-1, actualYear)
    }

    override fun onSelectedYear(year: Int) {
        txtAnio.setText(year.toString())
        txtFechaFin.setText(dmyFormatoFecha(1, 0, year))
        endStringDate = dataBaseFormatoFecha(1,0, year)
    }

    override fun onUserSelected(user: Int) {
        if (user == 0){
            txtUsuario.setText(idUsuario)
            userIdSelected = idUsuario
        }else{
            txtUsuario.setText(empleadoUsuario[user-1].nombre)
            userIdSelected = empleadoUsuario[user-1].id
        }
    }

    fun resRangeValues(){
        iniStringDate = dataBaseFormatoFecha(actualDay, actualMonth-1, actualYear)
        endStringDate = dataBaseFormatoFecha(actualDay, actualMonth-1, actualYear)
    }
}

