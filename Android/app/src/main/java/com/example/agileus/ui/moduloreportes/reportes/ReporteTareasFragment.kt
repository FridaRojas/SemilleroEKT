package com.example.agileus.ui.moduloreportes.reportes
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.format.DateFormat
import android.transition.TransitionInflater
import android.util.Half.toFloat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agileus.BuildConfig
import com.example.agileus.R
import com.example.agileus.config.InitialApplication.Companion.preferenciasGlobal
import com.example.agileus.databinding.ReporteTareasFragmentBinding
import com.example.agileus.models.UserTaskDetailReport
import com.example.agileus.providers.ReportesListener
import com.example.agileus.ui.HomeActivity
import com.example.agileus.ui.moduloreportes.dialogs.FiltroReportesDialog
import com.example.agileus.utils.Constantes
import com.example.agileus.utils.Constantes.taskStadisticData
import com.example.agileus.utils.Constantes.fechaFinEstadisticas
import com.example.agileus.utils.Constantes.fechaIniEstadisticas
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
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.URISyntaxException
import java.util.*
import kotlin.collections.ArrayList

class ReporteTareasFragment : Fragment(), ReportesListener, FiltroReportesDialog.FiltroReportesDialogListener  {

    private lateinit var reporteTareasViewModel: ReporteTareasViewModel
    private var _binding: ReporteTareasFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart

    //valores enteros de los datos de las tareas
    private var terminadas: Int = 0
    private var pendientes: Int = 0
    private var iniciada: Int = 0
    private var revision: Int = 0
    private var totales: Int = 0
    private var leidas: Int = 0
    private var terminadas_a_tiempo:Int=0
    private var terminadas_fuera_de_tiempo:Int=0

    //valores porcentuales de los datos de los mensajes para graficar
    private var porcentaje_termidas:Float = 0.0f
    private var porcentaje_pendientes:Float = 0.0f
    private var porcentaje_iniciada:Float = 0.0f
    private var porcentaje_revision:Float = 0.0f
    private var porcentaje_leidas:Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        reporteTareasViewModel = ViewModelProvider(this).get(ReporteTareasViewModel::class.java)
        _binding = ReporteTareasFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reporteTareasViewModel.devuelveListaEmpleados(preferenciasGlobal.recuperarIdSesion())

        reporteTareasViewModel.listaEmpleadosAux.observe(viewLifecycleOwner, { list->
            taskStadisticData = list
            binding.progressLoadingR.visibility = View.GONE
            binding.btnFiltroReportes.visibility = View.VISIBLE
            if (taskStadisticData.size == 1){
                idUsuarioEstadisticas = preferenciasGlobal.recuperarIdSesion()
                binding.txtNombreReportes.setText(preferenciasGlobal.recuperarNombreSesion())
            }else if (idUsuarioEstadisticas == Constantes.TEAM_ID_REPORTES && taskStadisticData.size > 1){
                idUsuarioEstadisticas = taskStadisticData[taskStadisticData.size - 1].id
                binding.txtNombreReportes.setText(taskStadisticData[taskStadisticData.size - 1].name)
            }
            setStadisticName()
            cambiarGrafica(tipo_grafica)

        })

        reporteTareasViewModel.cargaDatosExitosa.observe(viewLifecycleOwner, {

            iniciada = reporteTareasViewModel.iniciadas.value.toString().toInt()
            terminadas = reporteTareasViewModel.terminadas.value.toString().toInt()
            totales = reporteTareasViewModel.totales.value.toString().toInt()
            pendientes = reporteTareasViewModel.pendientes.value.toString().toInt()
            revision = reporteTareasViewModel.revision.value.toString().toInt()
            terminadas_a_tiempo = reporteTareasViewModel.aTiempo.value.toString().toInt()
            terminadas_fuera_de_tiempo = reporteTareasViewModel.fueraTiempo.value.toString().toInt()

        })

        binding.btnDownload.setOnClickListener {
            tomarScreenShot(view)
        }

        binding.btnFiltroReportes.setOnClickListener {
            val newFragment = FiltroReportesDialog(this)
            newFragment.show(requireActivity().supportFragmentManager, "Filtro de Reportes")
        }

        binding.btnReportesMensajes.setOnClickListener {
            val action = ReporteTareasFragmentDirections.actionReporteTareasFragmentToReporteMensajesFragment()
            val extras = FragmentNavigatorExtras(binding.btnReportesTareas to "report_slide")
            findNavController().navigate(action,  extras)
        }

        binding.barras.setOnClickListener {

            when(vista) {

                0 -> {

                    binding.barras.isVisible=false
                    binding.pie.isVisible=true
                    mostrargraficaBarras(2) //Aqui va la gráfica desglosada de tareas
                    binding.pieChart.isVisible = false
                    binding.barChart.isVisible = true
                    vista = 0
                    tipo_grafica = 1

                }

                1 -> {

                    binding.barras.isVisible=false
                    binding.pie.isVisible=true
                    cambiarGrafica(2)
                    binding.pieChart.isVisible = false
                    binding.barChart.isVisible = true
                    vista = 1
                    tipo_grafica = 1

                }


            }
        }

        binding.pie.setOnClickListener {
            binding.barras.isVisible=true
            binding.pie.isVisible=false
            cambiarGrafica(0)
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun setStadisticName(){
        taskStadisticData.forEach {
            if (idUsuarioEstadisticas == it.id){
                binding.txtNombreReportes.setText(it.name)
                Log.d("idUsuarioEstadisticas", it.id)
            }
        }
        if (binding.txtNombreReportes.text.isNullOrEmpty()){
            idUsuarioEstadisticas = Constantes.TEAM_ID_REPORTES
            reporteTareasViewModel.devuelveListaEmpleados(preferenciasGlobal.recuperarIdSesion())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrargraficaPie() {

        pieChart=binding.pieChart

        binding.colorlegend1.isVisible=true
        binding.colorlegend2.isVisible=true
        binding.colorlegend3.isVisible=true
        binding.colorlegend4.isVisible=true

        binding.txtPrimerLegend.text="Pendientes"
        binding.txtSegundoLegend.text="Iniciadas"
        binding.txtTercerLegend.text="Revisión"
        binding.txtCuartoLegend.text="Terminadas"

        binding.colorlegend1.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        binding.colorlegend2.setBackgroundColor(resources.getColor(R.color.colorSecondary))
        binding.colorlegend3.setBackgroundColor(resources.getColor(R.color.colorGray))
        binding.colorlegend4.setBackgroundColor(resources.getColor(android.R.color.holo_orange_dark))

        setStadisticName()
        binding.txtRangoFechaReportes.isVisible=false
        binding.txtRangoFechaReportes.setText(fechaIniEstadisticas + " " + fechaFinEstadisticas)

        reporteTareasViewModel.devuelvelistaReporte(this, idUsuarioEstadisticas)

        reporteTareasViewModel.adaptador.observe(viewLifecycleOwner,{
            binding.RecyclerLista.adapter = it
            binding.RecyclerLista.layoutManager = LinearLayoutManager(activity)
        })

            binding.txtDataPrimerLegend.text = pendientes.toString()
            binding.txtDataSegundoLegend.text = iniciada.toString()
            binding.txtDataTercerLegend.text = revision.toString()
            binding.txtDataCuartoLegend.text = terminadas.toString()

            porcentaje_termidas = obtenerPorcentajes(terminadas, totales)
            porcentaje_pendientes = obtenerPorcentajes(pendientes, totales)
            porcentaje_leidas = obtenerPorcentajes(leidas, totales)

            porcentaje_revision = obtenerPorcentajes(revision, totales)
            porcentaje_iniciada = obtenerPorcentajes(iniciada, totales)
            porcentaje_leidas = obtenerPorcentajes(leidas, totales)

            initPieChart()//inicializacion de la grafica de pie
            //aquí se agregan los valores porcentuales para su visualización
            setDataToPieChart(porcentaje_pendientes, porcentaje_iniciada, porcentaje_revision, porcentaje_termidas)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun mostrargraficaBarras(valor:Int) {

        barChart=binding.barChart
        setStadisticName()

        reporteTareasViewModel.devuelvelistaReporte(this, idUsuarioEstadisticas)

        reporteTareasViewModel.adaptador.observe(viewLifecycleOwner,{
            binding.RecyclerLista.adapter = it
            binding.RecyclerLista.layoutManager = LinearLayoutManager(activity)
        })

        //inicializacion de la grafica de barras y se agrega el objeto a desglosar para su visuzalización

        val sortedList = ArrayList(dataEmpleadoUsuario.sortedWith(compareBy { it.name }))

        if (valor==1){
            graficabarrasTareasATiempoGrupal(sortedList)
        }
        if (valor==2){
            graficabarrasDesgloseTareas(sortedList)
        }
        if (valor==3){
            graficabarrasDesgloseTareasATiempo(sortedList)
        }

    }

    private fun graficabarrasTareasATiempoGrupal(listaUsuarios: ArrayList<UserTaskDetailReport>) {

        binding.txtPrimerLegend.text="Finalizadas a tiempo"

        binding.txtTercerLegend.text="Finalizadas fuera de tiempo"

        binding.txtSegundoLegend.text = reporteTareasViewModel.aTiempo.value.toString()

        binding.txtCuartoLegend.text = reporteTareasViewModel.fueraTiempo.value.toString()

        binding.colorlegend3.setBackgroundColor(resources.getColor(R.color.colorSecondary))

        binding.colorlegend2.isVisible = false

        binding.colorlegend4.isVisible = false

        binding.txtDataPrimerLegend.text=""

        binding.txtDataSegundoLegend.text = ""

        binding.txtDataTercerLegend.text=""

        binding.txtDataCuartoLegend.text = ""

        val barChartView = binding.barChart

        val barWidth: Float = 0.15f //anchura de la barra
        val barSpace: Float = 0.10f // espacio entre las barras agrupadas
        val groupSpace: Float = 0.5f //espacio entre grupos de barras

        var xAxisValues = ArrayList<String>()

        var yValueGroup1 = ArrayList<BarEntry>()
        var yValueGroup2 = ArrayList<BarEntry>()

        // draw the graph
        var barDataSet1: BarDataSet
        var barDataSet2: BarDataSet

        var contador=0

        listaUsuarios.forEach {
            if(it.name == "Mi equipo"){
                xAxisValues.add(it.name)
                contador += 1

                //Estos son los datos verdaderos
                yValueGroup1.add((BarEntry(contador.toFloat(), it.onTime.toFloat())))
                yValueGroup2.add((BarEntry(contador.toFloat(), it.outTime.toFloat())))

                //Estos son datos de prueba
                //yValueGroup1.add((BarEntry(contador.toFloat(), 3f)))
                //yValueGroup2.add((BarEntry(contador.toFloat(), 6f)))
                //Aquí terminan los datos de prueba
            }
        }

        barDataSet1 = BarDataSet(yValueGroup1, "")
        barDataSet1.setColor(Color.parseColor("#66BB6A"))
        barDataSet1.setDrawIcons(false)
        barDataSet1.setDrawValues(false)

        barDataSet2 = BarDataSet(yValueGroup2, "")
        barDataSet2.setColor(Color.parseColor("#87D169"))
        barDataSet2.setDrawIcons(false)
        barDataSet2.setDrawValues(false)

        var barData = BarData(barDataSet1, barDataSet2)

        val xAxis = barChartView.xAxis
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(true)
        xAxis.textSize = 10f

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)

        //remove legenda
        barChartView.legend.isEnabled = false
        //remover etiqueta de descripción
        barChartView.description.isEnabled = false
        barChartView.description.textSize = 0f
        barData.setValueFormatter(LargeValueFormatter())
        barChartView.data = barData
        barChartView.barData.barWidth = barWidth
        barChartView.xAxis.axisMinimum = 0f
        barChartView.xAxis.axisMaximum = (0+barChartView.barData.getGroupWidth(groupSpace,barSpace) * contador) //mejora para el groupbarchart
        barChartView.groupBars(0f, groupSpace, barSpace)
        barChartView.setFitBars(true)
        barChartView.data.isHighlightEnabled = false
        barChartView.invalidate()

        barChart.animateY(1000)

        barChartView.setTouchEnabled(true)

        xAxis.labelCount = contador
        xAxis.mAxisMaximum = contador.toFloat()
        xAxis.setCenterAxisLabels(true)
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.spaceMin = 1f
        xAxis.spaceMax = 1f

        barChartView.setVisibleXRangeMaximum(1f)
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
        barChartView.setVisibleXRange(1f, 1f)

    }

    private fun graficabarrasDesgloseTareasATiempo(listaUsuarios: ArrayList<UserTaskDetailReport>) {

        binding.txtPrimerLegend.text="Finalizadas a tiempo"

        binding.txtTercerLegend.text="Finalizadas fuera de tiempo"

        binding.txtSegundoLegend.text = reporteTareasViewModel.aTiempo.value.toString()

        binding.txtCuartoLegend.text = reporteTareasViewModel.fueraTiempo.value.toString()

        binding.colorlegend3.setBackgroundColor(resources.getColor(R.color.colorSecondary))

        binding.colorlegend2.isVisible = false

        binding.colorlegend4.isVisible = false

        binding.txtDataPrimerLegend.text=""

        binding.txtDataSegundoLegend.text = ""

        binding.txtDataTercerLegend.text=""

        binding.txtDataCuartoLegend.text = ""

        val barChartView = binding.barChart

        val barWidth: Float = 0.15f //anchura de la barra
        val barSpace: Float = 0.10f // espacio entre las barras agrupadas
        val groupSpace: Float = 0.5f //espacio entre grupos de barras

        var xAxisValues = ArrayList<String>()

        var yValueGroup1 = ArrayList<BarEntry>()
        var yValueGroup2 = ArrayList<BarEntry>()

        // draw the graph
        var barDataSet1: BarDataSet
        var barDataSet2: BarDataSet

        var contador=0

        listaUsuarios.forEach {

            //Eston son los datos reales
            if ((it.name != "Mi equipo") && (it.name != "Mi información")){
                xAxisValues.add(it.name)
                contador += 1
                yValueGroup1.add((BarEntry(contador.toFloat(), it.onTime.toFloat())))
                yValueGroup2.add((BarEntry(contador.toFloat(), it.outTime.toFloat())))
            }

            //Estos son datos de prueba

            /*xAxisValues.add(it.name)
            contador += 1
            yValueGroup1.add((BarEntry(contador.toFloat(), 5f)))
            yValueGroup2.add((BarEntry(contador.toFloat(), 4f)))*/

            //Aquí terminan los datos de prueba

        }

        barDataSet1 = BarDataSet(yValueGroup1, "")
        barDataSet1.setColor(Color.parseColor("#66BB6A"))
        barDataSet1.setDrawIcons(false)
        barDataSet1.setDrawValues(false)

        barDataSet2 = BarDataSet(yValueGroup2, "")
        barDataSet2.setColor(Color.parseColor("#87D169"))
        barDataSet2.setDrawIcons(false)
        barDataSet2.setDrawValues(false)

        var barData = BarData(barDataSet1, barDataSet2)

        val xAxis = barChartView.xAxis
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(true)
        xAxis.textSize = 10f

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)

        //remove legenda
        barChartView.legend.isEnabled = false
        //remover etiqueta de descripción
        barChartView.description.isEnabled = false
        barChartView.description.textSize = 0f
        barData.setValueFormatter(LargeValueFormatter())
        barChartView.data = barData
        barChartView.barData.barWidth = barWidth
        barChartView.xAxis.axisMinimum = 0f
        barChartView.xAxis.axisMaximum = (0+barChartView.barData.getGroupWidth(groupSpace,barSpace) * contador) //mejora para el groupbarchart
        barChartView.groupBars(0f, groupSpace, barSpace)
        barChartView.setFitBars(true)
        barChartView.data.isHighlightEnabled = false
        barChartView.invalidate()

        barChart.animateY(1000)

        barChartView.setTouchEnabled(true)

        xAxis.labelCount = contador
        xAxis.mAxisMaximum = contador.toFloat()
        xAxis.setCenterAxisLabels(true)
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.spaceMin = 1f
        xAxis.spaceMax = 1f

        barChartView.setVisibleXRangeMaximum(1f)
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

    private fun graficabarrasDesgloseTareas(listaUsuarios: ArrayList<UserTaskDetailReport>) {

        val sortedList = ArrayList(listaUsuarios.sortedWith(compareBy { it.name }))

        binding.colorlegend1.isVisible=true
        binding.colorlegend2.isVisible=true
        binding.colorlegend3.isVisible=true
        binding.colorlegend4.isVisible=true

        binding.txtPrimerLegend.text="Pendientes"
        binding.txtSegundoLegend.text="Iniciadas"
        binding.txtTercerLegend.text="Revisión"
        binding.txtCuartoLegend.text="Terminadas"

        binding.colorlegend1.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        binding.colorlegend2.setBackgroundColor(resources.getColor(R.color.colorSecondary))
        binding.colorlegend3.setBackgroundColor(resources.getColor(R.color.colorGray))
        binding.colorlegend4.setBackgroundColor(resources.getColor(android.R.color.holo_orange_dark))

        binding.txtDataPrimerLegend.text = pendientes.toString()
        binding.txtDataSegundoLegend.text = iniciada.toString()
        binding.txtDataTercerLegend.text = revision.toString()
        binding.txtDataCuartoLegend.text = terminadas.toString()

                val barChartView = binding.barChart

        val barWidth: Float = 0.10f //anchura de la barra
        val barSpace: Float = 0.05f // espacio entre las barras agrupadas
        val groupSpace: Float = 0.4f //espacio entre grupos de barras

        var xAxisValues = ArrayList<String>()

        var yValueGroup1 = ArrayList<BarEntry>()
        var yValueGroup2 = ArrayList<BarEntry>()
        var yValueGroup3 = ArrayList<BarEntry>()
        var yValueGroup4 = ArrayList<BarEntry>()

        // draw the graph
        var barDataSet1: BarDataSet
        var barDataSet2: BarDataSet
        var barDataSet3: BarDataSet
        var barDataSet4: BarDataSet

        var contador=0

        sortedList.forEach {

            //Estos son los datos verdaderos
            if ((it.name != "Mi equipo") && (it.name != "Mi información")) {
                xAxisValues.add(it.name)
                contador += 1
                yValueGroup1.add((BarEntry(contador.toFloat(), it.pendings.toFloat())))
                yValueGroup2.add((BarEntry(contador.toFloat(), it.started.toFloat())))
                yValueGroup3.add((BarEntry(contador.toFloat(), it.revision.toFloat())))
                yValueGroup4.add((BarEntry(contador.toFloat(), it.finished.toFloat())))
            }

            //Estos son datos de prueba
                /*xAxisValues.add(it.name)
                contador += 1
                yValueGroup1.add((BarEntry(contador.toFloat(), 4f)))
                yValueGroup2.add((BarEntry(contador.toFloat(), 5f)))
                yValueGroup3.add((BarEntry(contador.toFloat(), 3f)))
                yValueGroup4.add((BarEntry(contador.toFloat(), 8f)))*/

            //Aquí terminan los datos de prueba

        }


        barDataSet1 = BarDataSet(yValueGroup1, "")
        barDataSet1.color = Color.parseColor("#66BB6A")
        barDataSet1.setDrawIcons(false)
        barDataSet1.setDrawValues(false)

        barDataSet2 = BarDataSet(yValueGroup2, "")
        barDataSet2.color = Color.parseColor("#87D169")
        barDataSet2.setDrawIcons(false)
        barDataSet2.setDrawValues(false)


        barDataSet3 = BarDataSet(yValueGroup3, "")
        barDataSet3.color = Color.parseColor("#7F8182")
        barDataSet3.setDrawIcons(false)
        barDataSet3.setDrawValues(false)

        barDataSet4 = BarDataSet(yValueGroup4, "")
        barDataSet4.color = Color.parseColor("#ffff8800")
        barDataSet4.setDrawIcons(false)
        barDataSet4.setDrawValues(false)


        var barData = BarData(barDataSet1, barDataSet2, barDataSet3, barDataSet4)

        val xAxis = barChartView.xAxis
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setCenterAxisLabels(true)
        xAxis.setDrawGridLines(true)
        xAxis.textSize = 10f

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)

        //remove legenda
        barChartView.legend.isEnabled = false
        //remover etiqueta de descripción
        barChartView.description.isEnabled = false
        barChartView.description.textSize = 0f
        barData.setValueFormatter(LargeValueFormatter())
        barChartView.data = barData
        barChartView.barData.barWidth = barWidth
        barChartView.xAxis.axisMinimum = 0f
        barChartView.xAxis.axisMaximum = (0+barChartView.barData.getGroupWidth(groupSpace,barSpace) * contador) //mejora para el groupbarchart
        barChartView.groupBars(0f, groupSpace, barSpace)
        barChartView.setFitBars(true)
        barChartView.data.isHighlightEnabled = false
        barChartView.invalidate()

        barChart.animateY(1000)

        barChartView.setTouchEnabled(true)

        xAxis.labelCount = contador
        xAxis.mAxisMaximum = contador.toFloat()
        xAxis.setCenterAxisLabels(true)
        xAxis.setAvoidFirstLastClipping(true)
        xAxis.spaceMin = 1f
        xAxis.spaceMax = 1f

        barChartView.setVisibleXRangeMaximum(2f)
        barChartView.setVisibleXRangeMinimum(2f)
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

    private fun setDataToPieChart(pendientes:Float,iniciadas:Float,revision:Float,terminadas:Float){
        pieChart.setUsePercentValues(true)
        val dataEntries = ArrayList<PieEntry>()
        dataEntries.add(PieEntry(pendientes, "Pendientes"))
        dataEntries.add(PieEntry(iniciadas, "Iniciadas"))
        dataEntries.add(PieEntry(revision, "Revision"))
        dataEntries.add(PieEntry(terminadas, "Terminadas"))

        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.colorPrimary))
        colors.add(resources.getColor(R.color.colorSecondary))
        colors.add(resources.getColor(R.color.colorGray))
        colors.add(resources.getColor(android.R.color.holo_orange_dark))

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDateFilterSelected() {
        Log.d("DateTASKFilter",  "User: ${idUsuarioEstadisticas}, iniCustom: ${fechaIniEstadisticas}, fecha: ${fechaFinEstadisticas}")

        if(idUsuarioEstadisticas == "TEAM_ID_CREATED_BY_MOD_REPORT"){
            reporteTareasViewModel.devuelveListaEmpleados(preferenciasGlobal.recuperarIdSesion())
        }else {
            cambiarGrafica(tipo_grafica)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun cambiarGrafica(valor: Int) {
        when (valor) {

            0 -> {

                if(binding.pie.isVisible){
                    binding.pie.isVisible=false
                    binding.barras.isVisible=true
                }

                mostrargraficaPie()
                binding.pieChart.isVisible = true
                binding.barChart.isVisible = false
                vista = 0
                tipo_grafica = 0
            }
            1 -> {

                mostrargraficaBarras(1)//Tareas terminadas a tiempo grupal
                binding.pieChart.isVisible = false
                binding.barChart.isVisible = true
                vista = 1
                tipo_grafica = 1

            }

            2 -> {

                mostrargraficaBarras(3)//Tareas terminadas a tiempo  desglosado
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





lateinit var pathImagen:String
@RequiresApi(Build.VERSION_CODES.Q)
fun tomarScreenShot(view:View){
    var fecha =  Date()
    var formato = DateFormat.format("yyyy-MM-dd_hh:mm:ss",fecha)


    view.isDrawingCacheEnabled = true
    val bitmap = Bitmap.createBitmap(view.getDrawingCache())
    view.isDrawingCacheEnabled = false


    if (Build.VERSION.SDK_INT >= 29) {
        //SC take and save
        var screeenReportUri = bitmap.saveImage(activity as HomeActivity)
        val screeenReport = getPath(activity as HomeActivity, screeenReportUri!!).toString()
        Log.e("RMF filePath", screeenReport)
        Toast.makeText(context, "Información guardada en galeria", Toast.LENGTH_SHORT).show()
    }else{
        try{
            val dirPath  = Environment.getExternalStorageDirectory().toString() + "/agileus"
            val fileDir = File(dirPath)
            if(!fileDir.exists()){
                val mkdir = fileDir.mkdir()
            }
            val path = "$dirPath/agileus-$formato.jpeg"
            pathImagen = path

            view.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(view.getDrawingCache())
            view.isDrawingCacheEnabled = false
            val imageFile = File(path)
            var fileOutputStream = FileOutputStream(imageFile)
            val calidad = 100
            bitmap.compress(Bitmap.CompressFormat.JPEG,calidad,fileOutputStream)
            //val bytes =  ByteArrayOutputStream()
            //fileOutputStream.write(bytes.toByteArray())
            fileOutputStream.flush()
            fileOutputStream.close()

            binding.imgCaptura.setImageBitmap(bitmap)

            val content = binding.imgCaptura as View
            content.isDrawingCacheEnabled = true

            val bitmapShare = content.drawingCache
            val root = Environment.getExternalStorageDirectory()
            val cachePath = File(root.absolutePath + "/AgileUs/report_${SystemClock.uptimeMillis()}")
            try {
                cachePath.createNewFile()
                val ostream = FileOutputStream(cachePath)
                bitmapShare.compress(Bitmap.CompressFormat.JPEG, 100, ostream)
                ostream.close()
            } catch (e: java.lang.Exception) {
                Log.e("Error mapa","Al crear bitmap")
                e.printStackTrace()
            }
            val imageUri = FileProvider.getUriForFile(
                Objects.requireNonNull(activity as HomeActivity),
                BuildConfig.APPLICATION_ID + ".provider", imageFile)

            Handler(Looper.getMainLooper()).postDelayed({
                var share =  Intent(Intent.ACTION_SEND)
                share.type = "image/*"
                share.putExtra(Intent.EXTRA_STREAM, imageUri)
                startActivity(Intent.createChooser(share,"Compartir captura"))
            },2000)
        }catch (e:Exception){
            Log.e("Error Captura",e.message.toString())
            e.printStackTrace()
        }

    }

}


@Throws(URISyntaxException::class)
fun getPath(context: Context, uri: Uri): String? {
    var uri = uri
    val needToCheckUri = Build.VERSION.SDK_INT >= 19
    var selection: String? = null
    var selectionArgs: Array<String>? = null
    if (needToCheckUri && DocumentsContract.isDocumentUri(context.applicationContext, uri)) {

        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":").toTypedArray()
        val type = split[0]
        if ("image" == type) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        } else if ("video" == type) {
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        } else if ("audio" == type) {
            uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
        selection = "_id=?"
        selectionArgs = arrayOf(split[1])
    }
    if ("content".equals(uri.scheme, ignoreCase = true)) {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        var cursor: Cursor? = null
        try {
            cursor =
                context.contentResolver.query(uri, projection, selection, selectionArgs, null)
            val column_index: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (cursor?.moveToFirst()!!) {
                return cursor.getString(column_index)
            }
        } catch (e: java.lang.Exception) {
        }
    } else if ("file".equals(uri.scheme, ignoreCase = true)) {
        return uri.path
    }
    return null
}

fun Bitmap.saveImage(context: Context): Uri? {
    val values = ContentValues()
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
    values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
    values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/AgileUs")
    values.put(MediaStore.Images.Media.IS_PENDING, true)
    values.put(MediaStore.Images.Media.DISPLAY_NAME, "report_${SystemClock.uptimeMillis()}")

    val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    if (uri != null) {
        saveImageToStream(this, context.contentResolver.openOutputStream(uri))
        values.put(MediaStore.Images.Media.IS_PENDING, false)
        context.contentResolver.update(uri, values, null, null)
        Log.d("URI", values.toString())
        return uri
    }
    return null
}


fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
    if (outputStream != null) {
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}



override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
}






}