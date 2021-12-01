package com.example.agileus.ui.moduloreportes.reportes
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.R
import com.example.agileus.config.MySharedPreferences
import com.example.agileus.config.MySharedPreferences.reportesGlobales.tipo_grafica
import com.example.agileus.config.MySharedPreferences.reportesGlobales.vista
import com.example.agileus.databinding.ReporteTareasFragmentBinding
import com.example.agileus.providers.ReportesListener
import com.example.agileus.ui.moduloreportes.dialogs.FiltroReportesDialog
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter

class ReporteTareasFragment : Fragment(), ReportesListener, FiltroReportesDialog.FiltroReportesDialogListener  {

    private lateinit var reporteTareasViewModel: ReporteTareasViewModel
    private var _binding: ReporteTareasFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart

    //valores enteros de los datos de los mensajes
    private var terminadas: Int = 0
    private var pendientes: Int = 0
    private var totales: Int = 0
    private var leidas: Int = 0

    //valores porcentuales de los datos de los mensajes para graficar
    private var porcentaje_termidas:Float = 0.0f
    private var porcentaje_pendientes:Float = 0.0f
    private var porcentaje_leidas:Float = 0.0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reporteTareasViewModel =
            ViewModelProvider(this).get(ReporteTareasViewModel::class.java)

        _binding = ReporteTareasFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnFiltroReportes.setOnClickListener {
            val newFragment = FiltroReportesDialog(this)
            newFragment.show(requireActivity().supportFragmentManager, "Filtro de Reportes")
        }

        binding.btnReportesMensajes.setOnClickListener {
            val action = ReporteTareasFragmentDirections.actionReporteTareasFragmentToReporteMensajesFragment()
            val extras = FragmentNavigatorExtras(binding.btnReportesTareas to "report_slide")
            findNavController().navigate(action,  extras)
        }


        cambiarGrafica(tipo_grafica)


    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrargraficaBarras() {

        barChart=binding.barChart

        binding.colorlegend1.isVisible=false

        binding.colorlegend2.isVisible=false

        reporteTareasViewModel.devuelvelistaReporte(this)

        reporteTareasViewModel.adaptador.observe(viewLifecycleOwner,{
            binding.RecyclerLista.adapter = it
            binding.RecyclerLista.layoutManager = LinearLayoutManager(activity)
        })

        reporteTareasViewModel.cargaDatosExitosa.observe(viewLifecycleOwner, {


            binding.txtDataPrimerLegend.text=""

            binding.txtDataSegundoLegend.text=""

            binding.txtPrimerLegend.text=""

            binding.txtSegundoLegend.text=""

            binding.txtTercerLegend.text="Terminadas"

            binding.txtCuartoLegend.text="Pendientes"

            binding.colorlegend3.setBackgroundColor(resources.getColor(R.color.colorPrimary))

            binding.colorlegend4.setBackgroundColor(resources.getColor(R.color.colorSecondary))


            binding.txtDataTercerLegend.text = reporteTareasViewModel.terminadas.value.toString()
            terminadas = reporteTareasViewModel.terminadas.value.toString().toInt()

            binding.txtDataCuartoLegend.text = reporteTareasViewModel.pendientes.value.toString()
            pendientes = reporteTareasViewModel.pendientes.value.toString().toInt()

            initBarChart(terminadas.toFloat(),pendientes.toFloat())//inicializacion de la grafica de barras
            // y se agregan los valores porcentuales para su visualización

        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrargraficaPie() {



        pieChart=binding.pieChart

        binding.colorlegend1.isVisible=true

        binding.colorlegend2.isVisible=true


        reporteTareasViewModel.devuelvelistaReporte(this)

        reporteTareasViewModel.adaptador.observe(viewLifecycleOwner,{
            binding.RecyclerLista.adapter = it
            binding.RecyclerLista.layoutManager = LinearLayoutManager(activity)
        })

        reporteTareasViewModel.cargaDatosExitosa.observe(viewLifecycleOwner, {

            binding.txtPrimerLegend.text="Termindas"

            binding.txtSegundoLegend.text="Pendientes"

            binding.txtTercerLegend.text="Totales"

            binding.txtCuartoLegend.text="Leídas"

            binding.colorlegend3.setBackgroundColor(resources.getColor(R.color.white))

            binding.colorlegend4.setBackgroundColor(resources.getColor(R.color.colorGray))

            binding.txtDataPrimerLegend.text = reporteTareasViewModel.terminadas.value.toString()
            terminadas = reporteTareasViewModel.terminadas.value.toString().toInt()

            binding.txtDataSegundoLegend.text = reporteTareasViewModel.pendientes.value.toString()
            pendientes = reporteTareasViewModel.pendientes.value.toString().toInt()

            binding.txtDataTercerLegend.text = reporteTareasViewModel.totales.value.toString()
            totales = reporteTareasViewModel.totales.value.toString().toInt()

            binding.txtDataCuartoLegend.text = reporteTareasViewModel.leidas.value.toString()
            leidas = reporteTareasViewModel.leidas.value.toString().toInt()

            porcentaje_termidas = obtenerPorcentajes(terminadas, totales)
            porcentaje_pendientes = obtenerPorcentajes(pendientes, totales)
            porcentaje_leidas = obtenerPorcentajes(leidas, totales)

            initPieChart()//inicializacion de la grafica de pie
            //aquí se agregan los valores porcentuales para su visualización
            setDataToPieChart(porcentaje_termidas, porcentaje_pendientes, porcentaje_leidas)

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
        dataEntries.add(PieEntry(enviados, "Terminadas"))
        dataEntries.add(PieEntry(recibidos, "Pendientes"))
        dataEntries.add(PieEntry(leidos, "Leídas"))

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

    private fun initBarChart(enviados:Float,recibidos:Float) {

        val entries: ArrayList<BarEntry> = ArrayList()
        entries.add(BarEntry(.5f, enviados))
        entries.add(BarEntry(1.5f, recibidos))

        val barDataSet = BarDataSet(entries, "")
        //barDataSet.setColors(*ColorTemplate.COLORFUL_COLORS)

        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.colorPrimary))
        colors.add(resources.getColor(R.color.colorSecondary))


        val data = BarData(barDataSet)
        barChart.data = data
        data.setBarWidth(0.3f);//Reducir el ancho de las barras
        barDataSet.colors = colors
        data.setValueTextSize(0f)

        //hide grid lines
        barChart.axisLeft.setDrawGridLines(true)
        barChart.xAxis.setDrawGridLines(true)
        barChart.xAxis.setDrawAxisLine(true)
        barChart.xAxis.isEnabled=false


        //remove right y-axis
        barChart.axisRight.isEnabled = false
        barChart.axisLeft.isEnabled = true

        //forzar a que la barra izquierda de la gráfica, muestre por valores enteros
        barChart.axisLeft.setGranularity(1.0f);
        barChart.axisLeft.setGranularityEnabled(true); // Required to enable granularity


        barChart.setTouchEnabled(false)

        //remove legend
        barChart.legend.isEnabled = false
        //remove description label
        barChart.description.isEnabled = false


        //add animation
        barChart.animateY(1000)


        //draw chart
        barChart.invalidate()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun cambiarGrafica(valor:Int) {

        when (valor) {

            0 -> {
                mostrargraficaPie()
                binding.pieChart.isVisible=true
                binding.barChart.isVisible=false
                vista = 0
                tipo_grafica=0
            }
            1 -> {

                mostrargraficaBarras()
                binding.barChart.isVisible=true
                binding.pieChart.isVisible=false
                vista = 1
                tipo_grafica=1

            }

            else -> {
                mostrargraficaPie()
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateFilterSelected() {
        cambiarGrafica(tipo_grafica)
        Toast.makeText(context, "User: ${MySharedPreferences.idUsuarioEstadisticas}, iniCustom: ${MySharedPreferences.fechaIniCustomEstadisticas}, fecha: ${MySharedPreferences.fechaEstadisticas}", Toast.LENGTH_SHORT).show()
        Log.d("DateFilter",  "User: ${MySharedPreferences.idUsuarioEstadisticas}, iniCustom: ${MySharedPreferences.fechaIniCustomEstadisticas}, fecha: ${MySharedPreferences.fechaEstadisticas}")
    }

}