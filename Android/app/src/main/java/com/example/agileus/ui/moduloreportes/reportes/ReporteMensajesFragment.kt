package com.example.agileus.ui.moduloreportes.reportes

import android.graphics.Color
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.R
import com.example.agileus.config.MySharedPreferences
import com.example.agileus.databinding.ReporteMensajesFragmentBinding
import com.example.agileus.ui.MainActivity
import com.example.agileus.ui.moduloreportes.dialogs.FiltroReportesDialog
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.components.LegendEntry
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit
import javax.xml.datatype.DatatypeConstants.DAYS




class ReporteMensajesFragment : Fragment(), FiltroReportesDialog.FiltroReportesDialogListener{

    private lateinit var reporteMensajesViewModel: ReporteMensajesViewModel
    private var _binding: ReporteMensajesFragmentBinding? = null
    private lateinit var pieChart: PieChart

    //valores enteros de los datos de los mensajes
    private var enviados: Int = 0
    private var recibidos: Int = 0
    private var totales: Int = 0
    private var leidos: Int = 0

    //valores porcentuales de los datos de los mensajes para graficar
    private var porcentaje_enviados:Float = 0.0f
    private var porcentaje_recibidos:Float = 0.0f
    private var porcentaje_leidos:Float = 0.0f


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reporteMensajesViewModel =
            ViewModelProvider(this).get(ReporteMensajesViewModel::class.java)

        _binding = ReporteMensajesFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFiltroReportes.setOnClickListener {
            val newFragment = FiltroReportesDialog(this)
            newFragment.show(requireActivity().supportFragmentManager, "Filtro de Reportes")
        }

        binding.btnReportesTareas.setOnClickListener {
            val action = ReporteMensajesFragmentDirections.actionReporteMensajesFragmentToReporteTareasFragment()
            val extras = FragmentNavigatorExtras(binding.btnReportesMensajes to "report_slide")
            findNavController().navigate(action,  extras)
        }

        pieChart = binding.pieChart

        reporteMensajesViewModel.devuelvelistaReporte()

        reporteMensajesViewModel.adaptador.observe(viewLifecycleOwner,{
            binding.RecyclerLista.adapter = it
            binding.RecyclerLista.layoutManager = LinearLayoutManager(activity)
        })

        reporteMensajesViewModel.cargaDatosExitosa.observe(viewLifecycleOwner, {

                binding.txtDataPrimerLegend.text = reporteMensajesViewModel.enviados.value.toString()
                enviados = reporteMensajesViewModel.enviados.value.toString().toInt()

                binding.txtDataSegundoLegend.text = reporteMensajesViewModel.recibidos.value.toString()
                recibidos = reporteMensajesViewModel.recibidos.value.toString().toInt()

                binding.txtDataTercerLegend.text = reporteMensajesViewModel.totales.value.toString()
                totales = reporteMensajesViewModel.totales.value.toString().toInt()

                binding.txtDataCuartoLegend.text = reporteMensajesViewModel.leidos.value.toString()
                leidos = reporteMensajesViewModel.leidos.value.toString().toInt()

                porcentaje_enviados = obtenerPorcentajes(enviados, totales)
                porcentaje_recibidos = obtenerPorcentajes(recibidos, totales)
                porcentaje_leidos = obtenerPorcentajes(leidos, totales)

                initPieChart()//inicializacion de la grafica de pie
                //aquí se agregan los valores porcentuales para su visualización
                setDataToPieChart(porcentaje_enviados, porcentaje_recibidos, porcentaje_leidos)

        })
    }

    //funcion regla de 3 para obtener un porcentage proporcional
    fun obtenerPorcentajes(dato_parcial:Int, dato_total:Int):Float{
        if(dato_total!=0) {
            return (dato_parcial*100/dato_total).toFloat()
        } else
        return 0.0f
    }

    private fun initPieChart() {

        pieChart.description.text = ""

        //hollow pie chart
        pieChart.isDrawHoleEnabled = false
        pieChart.setTouchEnabled(false)
        pieChart.setDrawEntryLabels(true)

        //pieChart.setUsePercentValues(true)
        pieChart.isRotationEnabled = false
        pieChart.setDrawEntryLabels(false)

        // Eliminar cuadritos
        pieChart.getLegend().isEnabled = false

        //Remove center chart text
        pieChart.setDrawSliceText(false)
    }

    private fun setDataToPieChart(enviados:Float,recibidos:Float,leidos:Float){
        pieChart.setUsePercentValues(true)
        val dataEntries = ArrayList<PieEntry>()
        dataEntries.add(PieEntry(enviados, "Enviados"))
        dataEntries.add(PieEntry(recibidos, "Recibidos"))
        dataEntries.add(PieEntry(leidos, "Leídos"))

        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.colorPrimary))
        colors.add(resources.getColor(R.color.colorSecondary))
        colors.add(resources.getColor(R.color.colorGray))

        val dataSet = PieDataSet(dataEntries, "")
        val data = PieData(dataSet)

        // In Percentage
        data.setValueFormatter(PercentFormatter())
        dataSet.colors = colors
        pieChart.data = data
        data.setValueTextSize(0f)
        pieChart.animateY(1000, Easing.EaseInOutQuad)

        //create hole in center
        pieChart.holeRadius = 58f
        pieChart.transparentCircleRadius = 61f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.TRANSPARENT)

        //add text in center
        pieChart.setDrawCenterText(false);

        pieChart.invalidate()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDayFilterSelected() {
        Toast.makeText(context, "Dia, userEST: ${MySharedPreferences.idUsuarioEstadisticas}, ini: ${MySharedPreferences.fechaInicioEstadisticas}, fin: ${MySharedPreferences.fechaFinEstadisticas}", Toast.LENGTH_SHORT).show()
    }

    override fun onMonthFilterSelected() {
        Toast.makeText(context, "Mes, userEST: ${MySharedPreferences.idUsuarioEstadisticas}, ini: ${MySharedPreferences.fechaInicioEstadisticas}, fin: ${MySharedPreferences.fechaFinEstadisticas}", Toast.LENGTH_SHORT).show()
    }

    override fun onYearFilterSelected() {
        Toast.makeText(context, "Año, userEST: ${MySharedPreferences.idUsuarioEstadisticas}, ini: ${MySharedPreferences.fechaInicioEstadisticas}, fin: ${MySharedPreferences.fechaFinEstadisticas}", Toast.LENGTH_SHORT).show()

    }

    override fun onCustomFilterSelected() {
        Toast.makeText(context, "Custom, userEST: ${MySharedPreferences.idUsuarioEstadisticas}, ini: ${MySharedPreferences.fechaInicioEstadisticas}, fin: ${MySharedPreferences.fechaFinEstadisticas}", Toast.LENGTH_SHORT).show()

    }

}