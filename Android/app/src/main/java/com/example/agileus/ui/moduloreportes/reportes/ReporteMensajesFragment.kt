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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.R
import com.example.agileus.databinding.ReporteMensajesFragmentBinding
import com.example.agileus.models.UserMessageDetailReport
import com.example.agileus.providers.ReportesListener
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.moduloreportes.dialogs.FiltroReportesDialog
import com.example.agileus.utils.Constantes
import com.example.agileus.utils.Constantes.empleadoUsuario
import com.example.agileus.utils.Constantes.idUsuarioEstadisticas
import com.example.agileus.utils.Constantes.tipo_grafica
import com.example.agileus.utils.Constantes.vista
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter

@RequiresApi(Build.VERSION_CODES.O)
class ReporteMensajesFragment : Fragment(), ReportesListener, FiltroReportesDialog.FiltroReportesDialogListener {

    private lateinit var reporteMensajesViewModel: ReporteMensajesViewModel
    private var _binding: ReporteMensajesFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var pieChart: PieChart  //Inicializacion para la gráfica de pie
    private lateinit var barChart: BarChart  //Inicializacion para la gráfica de barras

    //valores enteros de los datos de los mensajes
    private var enviados: Int = 0
    private var recibidos: Int = 0
    private var totales: Int = 0
    private var leidos: Int = 0

    //valores porcentuales de los datos de los mensajes para graficar
    private var porcentaje_enviados:Float = 0.0f
    private var porcentaje_recibidos:Float = 0.0f
    private var porcentaje_leidos:Float = 0.0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ReporteMensajesFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        reporteMensajesViewModel = ViewModelProvider(this).get(ReporteMensajesViewModel::class.java)

        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reporteMensajesViewModel.listaEmpleadosAux.observe(activity as HomeActivity, { list->
            empleadoUsuario = list
            //Toast.makeText(context, "PE: ${list}", Toast.LENGTH_LONG).show()
        })

        reporteMensajesViewModel.cargaOperacionesEstadisticas.observe(viewLifecycleOwner, {
        Toast.makeText(context, "COPE: ${reporteMensajesViewModel.cargaOperacionesEstadisticas.value}", Toast.LENGTH_LONG).show()
            binding.txtRangoFechaReportes.isVisible = true
            binding.txtRangoFechaReportes.setText("CargaCompleta")
        })

        reporteMensajesViewModel.devuelveListaEmpleados(Constantes.id)

        //información que nececita llevarse al diálogo
        binding.btnFiltroReportes.setOnClickListener {
            reporteMensajesViewModel.listaEmpleadosAux.observe(activity as HomeActivity, { list ->
                empleadoUsuario = list
            })
            val newFragment = FiltroReportesDialog(this)
            newFragment.show(requireActivity().supportFragmentManager, "Filtro de Reportes")
        }

        binding.btnReportesTareas.setOnClickListener {
            val action = ReporteMensajesFragmentDirections.actionReporteMensajesFragmentToReporteTareasFragment()
            val extras = FragmentNavigatorExtras(binding.btnReportesMensajes to "report_slide")
            findNavController().navigate(action, extras)
        }

        binding.barras.setOnClickListener {
            binding.barras.isVisible = false
            binding.pie.isVisible = true

            when (vista) {
                0 -> {
                    cambiarGrafica(1)
                    vista=0
                }
                1 -> {
                    cambiarGrafica(2)
                    vista=1
                }
            }
        }

        binding.pie.setOnClickListener {
            binding.barras.isVisible = true
            binding.pie.isVisible = false

            when (vista) {

                0 -> {
                    cambiarGrafica(0)
                    vista = 0
                }
                1 -> {
                    cambiarGrafica(0)
                    vista = 1
                }
            }

        }

            cambiarGrafica(tipo_grafica)
}

    fun mostrarRecyclerView(){

        binding.txtNombreReportes.setText(idUsuarioEstadisticas)
        reporteMensajesViewModel.devuelvelistaReporte(this, idUsuarioEstadisticas)

        reporteMensajesViewModel.adaptador.observe(viewLifecycleOwner, {
            binding.RecyclerLista.adapter = it
            binding.RecyclerLista.layoutManager = LinearLayoutManager(activity)
        })

    }

    fun obtenerDatos(){

        reporteMensajesViewModel.cargaDatosExitosa.observe(viewLifecycleOwner, {

            binding.colorlegend3.setBackgroundColor(resources.getColor(R.color.white))

            binding.colorlegend4.setBackgroundColor(resources.getColor(R.color.colorGray))

            binding.txtDataPrimerLegend.text = reporteMensajesViewModel.enviados.value.toString()
            enviados = reporteMensajesViewModel.enviados.value.toString().toInt()

            binding.txtDataSegundoLegend.text = reporteMensajesViewModel.recibidos.value.toString()
            recibidos = reporteMensajesViewModel.recibidos.value.toString().toInt()

            binding.txtDataTercerLegend.text = reporteMensajesViewModel.totales.value.toString()
            totales = reporteMensajesViewModel.totales.value.toString().toInt()

            binding.txtDataCuartoLegend.text = reporteMensajesViewModel.leidos.value.toString()
            leidos = reporteMensajesViewModel.leidos.value.toString().toInt()

            //aquí se obtinene el objeto de todos las estadísticas de mensajes
            //empleadoUsuario = reporteMensajesViewModel.listaEmpleadosAux.value as ArrayList

            porcentaje_enviados = obtenerPorcentajes(enviados, totales)
            porcentaje_recibidos = obtenerPorcentajes(recibidos, totales)
            porcentaje_leidos = obtenerPorcentajes(leidos, totales)

        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrargraficaBarras(valor:Int) {

        barChart = binding.barChart

        binding.txtPrimerLegend.text = "Enviados"
        binding.txtSegundoLegend.text = "Recibidos"
        binding.txtTercerLegend.text = ""
        binding.txtCuartoLegend.text = ""
        binding.colorlegend3.isVisible=false
        binding.colorlegend4.isVisible=false
        binding.txtDataTercerLegend.isVisible=false
        binding.txtDataCuartoLegend.isVisible=false

        obtenerDatos()
        mostrarRecyclerView()

        val sortedList = ArrayList(empleadoUsuario.sortedWith(compareBy { it.name }))

        initBarChart(sortedList,valor)//inicializacion de la grafica de barras
        // y se agregan los valores porcentuales para su visualización
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrargraficaPie() {

        pieChart = binding.pieChart

        binding.colorlegend3.isVisible=true
        binding.colorlegend4.isVisible=true
        binding.txtDataTercerLegend.isVisible=true
        binding.txtDataCuartoLegend.isVisible=true
        binding.txtPrimerLegend.text = "Enviados"
        binding.txtSegundoLegend.text = "Recibidos"
        binding.txtTercerLegend.text = "Totales"
        binding.txtCuartoLegend.text = "Leídos"

        obtenerDatos()
        mostrarRecyclerView()

        initPieChart()//inicializacion de la grafica de pie
        //aquí se agregan los valores porcentuales para su visualización
        setDataToPieChart(porcentaje_enviados, porcentaje_recibidos, porcentaje_leidos)
    }

    //funcion regla de 3 para obtener un porcentage proporcional
    fun obtenerPorcentajes(dato_parcial: Int, dato_total: Int): Float {
        if (dato_total != 0) {
            return (dato_parcial * 100 / dato_total).toFloat()
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

    private fun setDataToPieChart(enviados: Float, recibidos: Float, leidos: Float) {
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

    private fun initBarChart(listaUsuarios: ArrayList<UserMessageDetailReport>, valor:Int) {

        val barChartView = binding.barChart

        val barWidth: Float = 0.15f //anchura de la barra
        val barSpace: Float = 0.07f // espacio entre las barras agrupadas
        val groupSpace: Float = 0.56f //espacio entre grupos de barras

        var xAxisValues = ArrayList<String>()
        var yValueGroup1 = ArrayList<BarEntry>()
        var yValueGroup2 = ArrayList<BarEntry>()
        var yValueGroup3 = ArrayList<BarEntry>()

        // draw the graph
        var barDataSet1: BarDataSet
        var barDataSet2: BarDataSet
        var barDataSet3: BarDataSet

        var contador=0

        if(valor==0)
        {
          listaUsuarios.forEach {
              if(it.name == "Mi equipo"){
                  xAxisValues.add(it.name)
                  contador += 1
                  yValueGroup1.add((BarEntry(contador.toFloat(), it.send.toFloat())))
                  yValueGroup2.add((BarEntry(contador.toFloat(), it.received.toFloat())))
                  //yValueGroup3.add((BarEntry(contador.toFloat(), it.read.toFloat())))
              }
          }
        }

        if(valor==1) {
            listaUsuarios.forEach {
                if ((it.name != "Mi equipo") && (it.name != "Mi información")) {
                    xAxisValues.add(it.name)
                    contador += 1
                    yValueGroup1.add((BarEntry(contador.toFloat(), it.send.toFloat())))
                    yValueGroup2.add((BarEntry(contador.toFloat(), it.received.toFloat())))
                    //yValueGroup3.add((BarEntry(contador.toFloat(), it.read.toFloat())))
                }
            }
        }

        Log.d("contador",contador.toString())

        //xAxisValues.add("Usuario 1")
        //xAxisValues.add("Usuario 2")
        //xAxisValues.add("Usuario 3")
        //xAxisValues.add("Usuario 4")


        //yValueGroup1.add((BarEntry(1f, enviados)))
        //yValueGroup2.add((BarEntry(1f, recibidos)))
        //yValueGroup1.add((BarEntry(2f, enviados)))
        //yValueGroup2.add((BarEntry(2f, recibidos)))
        //yValueGroup1.add((BarEntry(3f, enviados)))
        //yValueGroup2.add((BarEntry(3f, recibidos)))
        //yValueGroup1.add((BarEntry(4f, enviados)))
        //yValueGroup2.add((BarEntry(4f, recibidos)))

        barDataSet1 = BarDataSet(yValueGroup1, "")
        barDataSet1.setColor(Color.parseColor("#66BB6A"))
        barDataSet1.setDrawIcons(false)
        barDataSet1.setDrawValues(false)

        barDataSet2 = BarDataSet(yValueGroup2, "")
        barDataSet2.setColor(Color.parseColor("#87D169"))
        barDataSet2.setDrawIcons(false)
        barDataSet2.setDrawValues(false)

        /*barDataSet3 = BarDataSet(yValueGroup3, "")
        barDataSet3.setColor(Color.YELLOW)
        barDataSet3.setDrawIcons(false)
        barDataSet3.setDrawValues(false)*/

        //var barData = BarData(barDataSet1, barDataSet2, barDataSet3)

        var barData = BarData(barDataSet1, barDataSet2)

        //remove legenda
        barChartView.legend.isEnabled = false
        //remover etiqueta de descripción
        barChartView.description.isEnabled = false
        barChartView.description.textSize = 0f
        barData.setValueFormatter(LargeValueFormatter())
        barChartView.data = barData
        barChartView.barData.barWidth = barWidth
        barChartView.xAxis.axisMinimum = 0f
        barChartView.xAxis.axisMaximum = 2f
        barChartView.groupBars(0f, groupSpace, barSpace)
        barChartView.setFitBars(true)
        barChartView.data.isHighlightEnabled = true
        barChartView.invalidate()

        //add animation
        barChart.animateY(1000)

        val xAxis = barChartView.xAxis
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(true)
        xAxis.textSize = 10f

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)

        barChartView.setTouchEnabled(true)

        xAxis.setLabelCount(contador)
        xAxis.mAxisMaximum = contador.toFloat()
        xAxis.setCenterAxisLabels(true)
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.spaceMin = 1f
        xAxis.spaceMax = 1f

        barChartView.setVisibleXRangeMaximum(2f)
        barChartView.setVisibleXRangeMinimum(1f)
        barChartView.isDragEnabled = true

        //Y-axis
        barChartView.axisRight.isEnabled = false
        barChartView.setScaleEnabled(true)

        val leftAxis = barChartView.axisLeft
        leftAxis.valueFormatter = LargeValueFormatter()
        leftAxis.setDrawGridLines(true)
        leftAxis.spaceTop = 1f
        leftAxis.axisMinimum = 0f

        barChartView.data = barData
        barChartView.setVisibleXRange(1f, 2f)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun cambiarGrafica(valor: Int) {

        when (valor) {

            0 -> {
                mostrargraficaPie()
                binding.pieChart.isVisible = true
                binding.barChart.isVisible = false
                vista = 0
                tipo_grafica = 0
            }
            1 -> {
                mostrargraficaBarras(0)
                binding.pieChart.isVisible = false
                binding.barChart.isVisible = true
                vista = 1
                tipo_grafica = 1
            }

            2 -> {
                mostrargraficaBarras(1)
                binding.pieChart.isVisible = false
                binding.barChart.isVisible = true
                vista = 1
                tipo_grafica = 1
            }

            else->{

                mostrargraficaPie()
                binding.pieChart.isVisible = true
                binding.barChart.isVisible = false
                vista = 0
                tipo_grafica = 0

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateFilterSelected() {
        cambiarGrafica(tipo_grafica)
        //Toast.makeText(context, "Opcion:${MySharedPreferences.opcionFiltro}, userEST: ${MySharedPreferences.idUsuarioEstadisticas}, ini: ${MySharedPreferences.fechaIniEstadisticas}, fin: ${MySharedPreferences.fechaFinEstadisticas}", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}