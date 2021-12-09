package com.example.agileus.ui.moduloreportes.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.DialogFragment
import com.example.agileus.R
import com.example.agileus.R.*
import com.example.agileus.config.MySharedPreferences
import com.example.agileus.databinding.DialogFiltroReportesBinding
import com.example.agileus.utils.Constantes
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.lang.Exception
import java.time.ZonedDateTime
import java.util.*

class FiltroReportesDialog(val listener: FiltroReportesDialogListener): DialogFragment(),
        DatePickerFiltroReportesDialogFragment.DatePickerFiltroReportesDialogListener,
        MonthPickerDialogFragment.MonthPickerDialogListener,
        YearPickerDialogFragment.YearPickerDialogListener,
        UserStadisticPickerDialogFragment.UserStadistickPickerDialogListener{

    private var _binding:DialogFiltroReportesBinding? = null
    private val binding get() = _binding!!

    private lateinit var tilFechaInicio: TextInputLayout
    private lateinit var txtFechaInicio: TextView
    private lateinit var tilFechaFin: TextInputLayout
    private lateinit var txtFechaFin: TextView
    private lateinit var tilDia: TextInputLayout
    private lateinit var txtDia: TextInputEditText
    private lateinit var tilMes: TextInputLayout
    private lateinit var txtMes: TextView
    private lateinit var tilAnio: TextInputLayout
    private lateinit var txtAnio: TextView
    private lateinit var txtTitulo: TextView
    private lateinit var txtUsuario: TextView
    private lateinit var iniStringDate: String
    private lateinit var endStringDate: String
    private lateinit var switchNoDateRange: SwitchCompat
    private var dateSelected = 0
    private var opcionFiltro = 0
    private var userIdSelected = Constantes.idUsuarioEstadisticas

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
            tilDia = vista.findViewById(R.id.tilFiltroDia)
            tilMes = vista.findViewById(R.id.tilFiltroMes)
            tilAnio = vista.findViewById(R.id.tilFiltroAnio)
            tilFechaInicio = vista.findViewById(R.id.tilNumeroFechaInicio)
            tilFechaFin = vista.findViewById(R.id.tilNumeroFechaFin)
            switchNoDateRange = vista.findViewById(R.id.switchFilterAllDataByID)

            val chipGroup = vista.findViewById<ChipGroup>(R.id.group)

            //txtTitulo.setText(MySharedPreferences.idUsuario)
            txtTitulo.setText("Filtrar")

            try {
                Constantes.dataEmpleadoUsuario.forEach {
                    if (Constantes.idUsuarioEstadisticas == it.id){
                        txtUsuario.setText(it.name)
                        Log.d("idUsuarioEstadisticas", it.id)
                    }
                }
            }catch (ex: Exception){
                Log.d("FRD Reporte", "DataNotLoadedYet")
                //txtUsuario.setText("Mi informacion")
                txtUsuario.setText(Constantes.idUsuarioEstadisticas)
            }

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
            tilFechaInicio.visibility = View.GONE
            tilFechaFin.visibility = View.GONE
            tilMes.visibility = View.GONE
            tilAnio.visibility = View.GONE

            iniStringDate = dataBaseFormatoFecha(actualDay, actualMonth-1, actualYear)
            endStringDate = dataBaseFormatoFecha(actualDay+1, actualMonth-1, actualYear)

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
                        //resRangeValues()
                        tilDia.visibility = View.VISIBLE
                        tilFechaInicio.visibility = View.GONE
                        tilFechaFin.visibility = View.GONE
                        tilMes.visibility = View.GONE
                        tilAnio.visibility = View.GONE

                        txtInicio.setText("Dia:")
                        txtDia.visibility = View.VISIBLE
                        txtDia.setText(formatoDiaSelected(actualDay, actualMonth, actualYear, fechaActual))
                        iniStringDate = dataBaseFormatoFecha(actualDay, actualMonth-1, actualYear)
                        endStringDate = dataBaseFormatoFecha(actualDay+1, actualMonth-1, actualYear)

                    }
                    R.id.chipFiltroMes -> {
                        opcionFiltro = 2
                        txtFin.visibility =View.GONE
                        txtFechaFin.visibility =View.GONE
                        txtFechaInicio.visibility =View.GONE
                        txtAnio.visibility =View.GONE
                        txtDia.visibility = View.GONE
                        tilDia.visibility = View.GONE
                        txtFechaFin.setText(dmyFormatoFecha(1, actualMonth-1, actualYear))
                        //resRangeValues()
                        tilAnio.visibility = View.GONE
                        tilFechaInicio.visibility = View.GONE
                        tilFechaFin.visibility = View.GONE

                        txtInicio.setText("Mes:")
                        txtMes.visibility =View.VISIBLE
                        tilMes.visibility = View.VISIBLE
                        iniStringDate = dataBaseFormatoFecha(1, actualMonth-1, actualYear)
                        endStringDate = dataBaseFormatoFecha(1, actualMonth, actualYear)

                    }
                    R.id.chipFiltroAnio -> {
                        opcionFiltro = 3
                        txtFin.visibility =View.GONE
                        txtFechaFin.visibility =View.GONE
                        txtFechaInicio.visibility =View.GONE
                        txtMes.visibility =View.GONE
                        txtDia.visibility = View.GONE
                        txtFechaFin.setText(dmyFormatoFecha(1, 0, actualYear))
                        //resRangeValues()
                        tilDia.visibility = View.GONE
                        tilMes.visibility = View.GONE
                        tilFechaInicio.visibility = View.GONE
                        tilFechaFin.visibility = View.GONE

                        txtInicio.setText("Año:")
                        txtAnio.visibility =View.VISIBLE
                        tilAnio.visibility =View.VISIBLE
                        txtAnio.setText(actualYear.toString())
                        iniStringDate = dataBaseFormatoFecha(1, 0, actualYear)
                        endStringDate = dataBaseFormatoFecha(1, 0, actualYear+1)
                    }
                    R.id.chipFiltroPersonalizado -> {
                        opcionFiltro = 4
                        txtFin.visibility =View.VISIBLE
                        txtFechaFin.visibility =View.VISIBLE
                        txtFechaInicio.visibility = View.VISIBLE
                        txtMes.visibility =View.GONE
                        txtAnio.visibility =View.GONE
                        txtDia.visibility = View.GONE
                        //resRangeValues()
                        tilDia.visibility = View.GONE
                        tilMes.visibility = View.GONE
                        tilAnio.visibility = View.GONE

                        txtInicio.setText("Fecha de inicio:")
                        tilFechaFin.visibility =View.VISIBLE
                        tilFechaInicio.visibility = View.VISIBLE
                        txtFechaInicio.setText(fechaActual)
                        txtFechaFin.setText(dmyFormatoFecha(actualDay, actualMonth-1, actualYear))
                        iniStringDate = dataBaseFormatoFecha(actualDay, actualMonth-1, actualYear)
                        endStringDate = dataBaseFormatoFecha(actualDay+1, actualMonth-1, actualYear)
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
            txtUsuario.setOnClickListener {
                val newFragment = UserStadisticPickerDialogFragment(this)
                newFragment.show(requireActivity().supportFragmentManager, "Filtro de Reportes")
            }

            builder.setView(vista)
                .setPositiveButton("Aceptar",
                    DialogInterface.OnClickListener { dialog, id ->

                        if (switchNoDateRange.isChecked){
                            iniStringDate = "1900-01-01T00:00:00.000+00:00"
                            endStringDate = "2100-01-01T00:00:00.000+00:00"
                            Toast.makeText(context, "Activado", Toast.LENGTH_SHORT).show()
                        }
                        Constantes.fechaIniEstadisticas = iniStringDate
                        Constantes.fechaFinEstadisticas = endStringDate
                        Constantes.idUsuarioEstadisticas = userIdSelected

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

        return asMonth.replaceFirstChar {  it.uppercaseChar() }
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
        return "${nombreDiaDelAnio(dia, mes, anio).replaceFirstChar {  it.uppercaseChar() }}: ${currentDF} "
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateFiltroReportesSelected(anio: Int, mes: Int, dia: Int){
        val currentDate = dmyFormatoFecha(dia, mes, anio)//= sdf.format(d)
        txtDia.setText(formatoDiaSelected(dia, mes+1, anio, currentDate))
        if(dateSelected == 0 && opcionFiltro == 4){
            if(ZonedDateTime.parse(endStringDate).isAfter(ZonedDateTime.parse(dataBaseFormatoFecha(dia, mes, anio)))){
                txtFechaInicio.setText(currentDate)
                iniStringDate = dataBaseFormatoFecha(dia, mes, anio)
                //endStringDate = dataBaseFormatoFecha(actualDay+1, actualMonth-1, actualYear)
            }else{
                Toast.makeText(context, "La fecha debe ser anterior a la final", Toast.LENGTH_SHORT).show()
            }
        }else if (dateSelected == 0){
            txtFechaInicio.setText(formatoDiaSelected(dia, mes, anio, currentDate))
            iniStringDate = dataBaseFormatoFecha(dia, mes, anio)
            endStringDate = dataBaseFormatoFecha(dia+1, mes, anio)
        }else{
            if(ZonedDateTime.parse(iniStringDate).isBefore(ZonedDateTime.parse(dataBaseFormatoFecha(dia+1, mes, anio))) ){
                //Toast.makeText(context, "ini: $iniStringDate , fin ${ZonedDateTime.parse(dataBaseFormatoFecha(dia + 1, mes, anio))}", Toast.LENGTH_LONG).show()
                txtFechaFin.setText(currentDate)
                endStringDate = dataBaseFormatoFecha(dia+1, mes, anio)
            }else{
                Toast.makeText(context, "La fecha debe ser posterior a la de inicio", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMonthSelected(mes: Int) {
        txtMes.setText(mesDelAnio(actualDay, mes, actualYear))
        txtFechaFin.setText(dmyFormatoFecha(1, mes-1, actualYear))
        iniStringDate = dataBaseFormatoFecha(1, mes-1, actualYear)
        endStringDate = dataBaseFormatoFecha(1, mes, actualYear)
    }

    override fun onSelectedYear(year: Int) {
        txtAnio.setText(year.toString())
        txtFechaFin.setText(dmyFormatoFecha(1, 0, year))
        iniStringDate = dataBaseFormatoFecha(1,0, year)
        endStringDate = dataBaseFormatoFecha(1,0, year+1)
    }

    override fun onUserSelected(user: Int) {


        if(Constantes.dataEmpleadoUsuario.size == 0){
            txtUsuario.setText(Constantes.empleadoUsuario[user].name)
            userIdSelected = Constantes.empleadoUsuario[user].id
        }else{

            txtUsuario.setText(Constantes.dataEmpleadoUsuario[user].name)
            userIdSelected = Constantes.dataEmpleadoUsuario[user].id
        }

        /*

        if (user == 0){
            txtUsuario.setText(idUsuario)
            userIdSelected = idUsuario
        }else{
            txtUsuario.setText(dataEmpleadoUsuario[user-1].name)
            userIdSelected = dataEmpleadoUsuario[user-1].id
        }
         */
    }

    fun resRangeValues(){
        iniStringDate = dataBaseFormatoFecha(actualDay, actualMonth-1, actualYear)
        endStringDate = dataBaseFormatoFecha(actualDay, actualMonth-1, actualYear)
    }
}

